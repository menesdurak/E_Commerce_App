package com.menesdurak.e_ticaret_uygulamasi.data.repository

import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val dao: ETicaretDao) : LocalRepository {
    override suspend fun getAllFavoriteProducts(): List<FavoriteProduct> {
        return dao.getAllFavoriteProducts()
    }

    override suspend fun addFavoriteProduct(favoriteProduct: FavoriteProduct) {
        dao.addFavoriteProduct(favoriteProduct)
    }

    override suspend fun deleteFavoriteProductWithId(favoriteProductId: Int) {
        dao.deleteFavoriteProductWithId(favoriteProductId)
    }
}