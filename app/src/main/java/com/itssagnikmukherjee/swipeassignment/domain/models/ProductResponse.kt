package com.itssagnikmukherjee.swipeassignment.domain.models

data class ProductResponse(
    val image: String?,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)