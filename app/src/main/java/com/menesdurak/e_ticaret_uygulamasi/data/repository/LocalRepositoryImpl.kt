package com.menesdurak.e_ticaret_uygulamasi.data.repository

import com.menesdurak.e_ticaret_uygulamasi.data.local.CartDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.CreditCardDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val eTicaretDao: ETicaretDao,
    private val cartDao: CartDao,
    private val creditCardDao: CreditCardDao
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

    override suspend fun deleteAllCheckedCartProducts() {
        cartDao.deleteAllCheckedCartProducts()
    }

    override suspend fun updateAllCartProductsToChecked() {
        cartDao.updateAllCartProductsToChecked()
    }

    override suspend fun updateAllCartProductsToNotChecked() {
        cartDao.updateAllCartProductsToNotChecked()
    }

    override suspend fun addCreditCard(creditCardInfo: CreditCardInfo) {
        creditCardDao.addCreditCard(creditCardInfo)
    }

    override suspend fun deleteCreditCardWithId(creditCardId: Short) {
        creditCardDao.deleteCreditCardWithId(creditCardId)
    }

    override suspend fun deleteAllCreditCards() {
        creditCardDao.deleteAllCreditCards()
    }

    override suspend fun getAllCreditCards(): List<CreditCardInfo> {
        return creditCardDao.getAllCreditCards()
    }

    override suspend fun updateCreditCardActiveStatus(isActive: Boolean, creditCardId: Short) {
        updateCreditCardActiveStatus(isActive, creditCardId)
    }
}