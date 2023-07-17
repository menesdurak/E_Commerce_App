package com.menesdurak.e_ticaret_uygulamasi.data.remote.api

import retrofit2.http.GET

interface ETicaretApi {

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>
}