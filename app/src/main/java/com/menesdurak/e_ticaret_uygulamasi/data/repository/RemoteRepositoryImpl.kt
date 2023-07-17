package com.menesdurak.e_ticaret_uygulamasi.data.repository

import com.menesdurak.e_ticaret_uygulamasi.data.remote.api.ETicaretApi
import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.Product
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.RemoteRepository
import javax.inject.Inject

class RemoteRepositoryImpl @Inject constructor(private val api: ETicaretApi): RemoteRepository {
    override suspend fun getAllCategories(): List<String> {
        return api.getAllCategories()
    }

    override suspend fun getProductsFromCategory(categoryName: String): List<Product> {
        return api.getProductsFromCategory(categoryName)
    }
}