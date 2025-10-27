package com.itssagnikmukherjee.swipeassignment.domain.repo

import android.content.Context
import android.net.Uri
import com.itssagnikmukherjee.swipeassignment.data.api.SwipeApi
import com.itssagnikmukherjee.swipeassignment.data.local.dao.CachedProductDao
import com.itssagnikmukherjee.swipeassignment.data.local.dao.PendingProductsDao
import com.itssagnikmukherjee.swipeassignment.data.local.tables.CachedProduct
import com.itssagnikmukherjee.swipeassignment.data.local.tables.PendingProduct
import com.itssagnikmukherjee.swipeassignment.data.local.tables.toCachedProduct
import com.itssagnikmukherjee.swipeassignment.data.local.tables.toProductResponse
import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse
import com.itssagnikmukherjee.swipeassignment.utils.NetworkConnectivityObserver
import com.itssagnikmukherjee.swipeassignment.utils.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class ProductRepo(
    private val api: SwipeApi,
    private val cachedProductDao: CachedProductDao,
    private val pendingProductDao: PendingProductsDao,
    private val connectivityObserver: NetworkConnectivityObserver
) {
    // Get products with offline support
    suspend fun getProducts(): Response<List<ProductResponse>> {
        return if (connectivityObserver.isConnected()) {
            try {
                val response = api.getProducts()
                if (response.isSuccessful) {
                    cachedProductDao.clearCache()
                    response.body()?.let { products ->
                        val cachedProducts = products.mapIndexed { index, product ->
                            product.toCachedProduct().copy(
                                lastUpdated = System.currentTimeMillis(),
                                orderIndex = index
                            )
                        }
                        cachedProductDao.insertProducts(cachedProducts)
                    }
                }
                response
            } catch (e: Exception) {
                e.printStackTrace()
                getCachedProductsAsResponse()
            }
        } else {
            getCachedProductsAsResponse()
        }
    }

    suspend fun getPendingProductsList(): List<PendingProduct> {
        return pendingProductDao.getPendingProductsList()
    }

    private suspend fun getCachedProductsAsResponse(): Response<List<ProductResponse>> {
        val cachedProducts = cachedProductDao.getAllCachedProductsList()
        val productResponses = cachedProducts.map { it.toProductResponse() }
        return Response.success(productResponses)
    }

    // Add product with offline support
    suspend fun addProduct(
        name: String,
        type: String,
        price: String,
        tax: String,
        imageUri: Uri? = null,
        context: Context
    ): Response<Map<String, Any>> {
        return if (connectivityObserver.isConnected()) {
            uploadProductToServer(name, type, price, tax, imageUri, context)
        } else {
            val pendingProduct = PendingProduct(
                productName = name,
                productType = type,
                price = price,
                tax = tax,
                imageUri = imageUri?.toString(),
                syncStatus = SyncStatus.PENDING
            )
            pendingProductDao.insertPendingProduct(pendingProduct)
            Response.success(mapOf("message" to "Product saved offline. Will sync when online."))
        }
    }

    private suspend fun uploadProductToServer(
        name: String,
        type: String,
        price: String,
        tax: String,
        imageUri: Uri?,
        context: Context
    ): Response<Map<String, Any>> {
        val productNameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val productTypeBody = type.toRequestBody("text/plain".toMediaTypeOrNull())
        val priceBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val taxBody = tax.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = imageUri?.let { uri ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null) {
                    val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("files[]", "image.jpg", requestBody)
                } else null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        return api.addProduct(productNameBody, productTypeBody, priceBody, taxBody, imagePart)
    }

    // Sync pending products
    val mutex = Mutex()
    suspend fun syncPendingProducts(context: Context): Result<Int> {
        if (!connectivityObserver.isConnected()) {
            return Result.failure(Exception("No internet connection"))
        }

        val pendingProducts = pendingProductDao.getPendingProductsList()
        var successCount = 0
        var failCount = 0

        if(mutex.tryLock()) {
            try {
                pendingProducts.forEach { pending ->
                    try {
                        pendingProductDao.updateSyncStatus(pending.id, SyncStatus.SYNCING)

                        val imageUri = pending.imageUri?.let { Uri.parse(it) }
                        val response = uploadProductToServer(
                            name = pending.productName,
                            type = pending.productType,
                            price = pending.price,
                            tax = pending.tax,
                            imageUri = imageUri,
                            context = context
                        )

                        if (response.isSuccessful) {
                            pendingProductDao.deletePendingProduct(pending.id)
                            successCount++
                        } else {
                            pendingProductDao.updateSyncStatus(pending.id, SyncStatus.FAILED)
                            failCount++
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        pendingProductDao.updateSyncStatus(pending.id, SyncStatus.FAILED)
                        failCount++
                    }
                }
            }finally {
                mutex.unlock()
            }
        }

        return if (failCount == 0) {
            Result.success(successCount)
        } else {
            Result.failure(Exception("Synced: $successCount, Failed: $failCount"))
        }
    }

    fun getPendingProducts(): Flow<List<PendingProduct>> = pendingProductDao.getPendingProducts()

    fun getCachedProducts(): Flow<List<CachedProduct>> = cachedProductDao.getAllCachedProducts()
}