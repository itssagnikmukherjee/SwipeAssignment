package com.itssagnikmukherjee.swipeassignment.data.local.tables
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse

@Entity(tableName = "cached_products")
data class CachedProduct(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val productName: String,
    val productType: String,
    val price: Double,
    val tax: Double,
    val image: String?,
    val lastUpdated: Long = System.currentTimeMillis(),
    val orderIndex: Int = 0
)

fun ProductResponse.toCachedProduct() = CachedProduct(
    productName = product_name,
    productType = product_type,
    price = price,
    tax = tax,
    image = image,
    lastUpdated = System.currentTimeMillis()
)

fun CachedProduct.toProductResponse() = ProductResponse(
    image = image,
    price = price,
    product_name = productName,
    product_type = productType,
    tax = tax,
)