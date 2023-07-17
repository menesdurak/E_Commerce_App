package com.menesdurak.e_ticaret_uygulamasi.data.remote.api

import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ETicaretApi {

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products/category/{category_name}")
    suspend fun getProductsFromCategory(@Path("category_name") categoryName: String): List<Product>
}