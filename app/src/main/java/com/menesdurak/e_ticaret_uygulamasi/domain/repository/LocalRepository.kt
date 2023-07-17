package com.menesdurak.e_ticaret_uygulamasi.domain.repository

import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct

interface LocalRepository {

    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>

    suspend fun addFavoriteProduct(favoriteProduct: FavoriteProduct)

    suspend fun deleteFavoriteProductWithId(favoriteProductId: Int)
}