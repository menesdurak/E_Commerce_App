package com.menesdurak.e_ticaret_uygulamasi.domain.repository

import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct

interface LocalRepository {

    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>

    suspend fun addFavoriteProduct(favoriteProduct: FavoriteProduct)

    suspend fun deleteFavoriteProductWithId(favoriteProductId: Int)

    suspend fun getAllCartProducts(): List<CartProduct>

    suspend fun addCartProduct(cartProduct: CartProduct)

    suspend fun deleteCartProductWithId(cartProductId: Int)

    suspend fun deleteAllCartProducts()

    suspend fun updateCartProductCheckedStatus(isChecked: Boolean, cartProductId: Int)

    suspend fun updateCartProductAmount(newAmount: Int, cartProductId: Int)
}