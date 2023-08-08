package com.menesdurak.e_ticaret_uygulamasi.domain.repository

import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product

interface RemoteRepository {

    suspend fun getAllCategories(): List<String>

    suspend fun getProductsFromCategory(categoryName: String): List<Product>

    suspend fun getAllProducts(): List<Product>

    suspend fun getSingleProduct(productId: Int): Product
}