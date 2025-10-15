package com.itssagnikmukherjee.swipeassignment.ui.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse
import com.itssagnikmukherjee.swipeassignment.domain.repo.ProductRepo
import com.itssagnikmukherjee.swipeassignment.ui.screens.ProductListScreen
import com.itssagnikmukherjee.swipeassignment.utils.ConnectivityObserver
import com.itssagnikmukherjee.swipeassignment.utils.NetworkConnectivityObserver
import com.itssagnikmukherjee.swipeassignment.utils.NotificationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val repo: ProductRepo,
    private val connectivityObserver: NetworkConnectivityObserver,
    private val application: Application
) : ViewModel() {
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state

    private val _pendingProductsCount = MutableStateFlow(0)
    val pendingProductsCount: StateFlow<Int> = _pendingProductsCount

    private val notificationHelper = NotificationHelper(application)

    init {
        _state.value = _state.value.copy(isOnline = connectivityObserver.isConnected())
        getProducts()
        observeConnectivity()
        observePendingProducts()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.observe()
                .debounce(1000)
                .collect { status ->
                    val isOnline = status == ConnectivityObserver.Status.AVAILABLE
                    _state.value = _state.value.copy(isOnline = isOnline)

                    if (isOnline && _pendingProductsCount.value > 0) {
                        syncPendingProducts()
                    }
                }
        }
    }

    private fun observePendingProducts() {
        viewModelScope.launch {
            repo.getPendingProducts().collect { pendingProducts ->
                _pendingProductsCount.value = pendingProducts.size
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val response = repo.getProducts()
            if (response.isSuccessful) {
                val products = response.body() ?: emptyList()

                val categories = listOf("All") + products
                    .map { it.product_type }
                    .distinct()
                    .sorted()

                _state.value = _state.value.copy(
                    isLoading = false,
                    products = products,
                    categories = categories,
                    error = null
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load products"
                )
            }
        }
    }

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageUri: Uri? = null,
        context: Context,
        onSuccess: (String) -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val isOnline = _state.value.isOnline
                val response = repo.addProduct(productName, productType, price, tax, imageUri, context)

                if (response.isSuccessful) {
                    val message = if (isOnline) {
                        notificationHelper.showProductAddedNotification(productName)
                        "Product added successfully"
                    } else {
                        notificationHelper.showProductPendingNotification(productName)
                        "Upload is pending"
                    }
                    getProducts()
                    _state.value = _state.value.copy(isLoading = false)
                    onSuccess(message)
                } else {
                    val errorMsg = "Failed to add product: ${response.code()}"
                    _state.value = _state.value.copy(isLoading = false, error = errorMsg)
                    onError(errorMsg)
                }
            } catch (e: Exception) {
                val errorMsg = "Error: ${e.message}"
                _state.value = _state.value.copy(isLoading = false, error = errorMsg)
                onError(errorMsg)
            }
        }
    }

    fun syncPendingProducts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSyncing = true)
            val result = repo.syncPendingProducts(application)
            _state.value = _state.value.copy(isSyncing = false)

            result.onSuccess { count ->
                if (count > 0) {
                    notificationHelper.showSyncCompleteNotification(count)
                }
                _state.value = _state.value.copy(
                    syncMessage = if (count > 0) "Uploading successful" else "No pending products to sync"
                )
                getProducts()
            }.onFailure { error ->
                _state.value = _state.value.copy(
                    syncMessage = "Sync failed: ${error.message}"
                )
            }
        }
    }
}

data class ProductListState(
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false,
    val isOnline: Boolean = true,
    val products: List<ProductResponse> = emptyList(),
    val categories: List<String> = emptyList(),
    val error: String? = null,
    val syncMessage: String? = null
)