package com.menesdurak.e_ticaret_uygulamasi.data.repository

import com.menesdurak.e_ticaret_uygulamasi.data.local.CartDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val eTicaretDao: ETicaretDao,
    private val cartDao: CartDao,
) : LocalRepository {
    override suspend fun getAllFavoriteProducts(): List<FavoriteProduct> {
        return eTicaretDao.getAllFavoriteProducts()
    }

    override suspend fun addFavoriteProduct(favoriteProduct: FavoriteProduct) {
        eTicaretDao.addFavoriteProduct(favoriteProduct)
    }

    override suspend fun deleteFavoriteProductWithId(favoriteProductId: Int) {
        eTicaretDao.deleteFavoriteProductWithId(favoriteProductId)
    }

    override suspend fun getAllCartProducts(): List<CartProduct> {
        return cartDao.getAllCartProducts()
    }

    override suspend fun addCartProduct(cartProduct: CartProduct) {
        cartDao.addCartProduct(cartProduct)
    }

    override suspend fun deleteCartProductWithId(cartProductId: Int) {
        cartDao.deleteCartProductWithId(cartProductId)
    }

    override suspend fun deleteAllCartProducts() {
        cartDao.deleteAllCartProducts()
    }

    override suspend fun updateCartProductCheckedStatus(isChecked: Boolean, cartProductId: Int) {
        cartDao.updateCartProductCheckedStatus(isChecked, cartProductId)
    }

    override suspend fun updateCartProductAmount(newAmount: Int, cartProductId: Int) {
        cartDao.updateCartProductAmount(newAmount, cartProductId)
    }
}