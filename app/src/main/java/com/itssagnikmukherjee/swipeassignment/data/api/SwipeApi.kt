package com.itssagnikmukherjee.swipeassignment.data.api

import com.itssagnikmukherjee.swipeassignment.domain.models.ProductResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.nio.file.Files

interface SwipeApi {
    @GET("get")
    suspend fun getProducts() : Response<List<ProductResponse>>

    @Multipart
    @POST("add")
    suspend fun addProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: MultipartBody.Part? = null
    ): Response<Map<String, Any>>
}