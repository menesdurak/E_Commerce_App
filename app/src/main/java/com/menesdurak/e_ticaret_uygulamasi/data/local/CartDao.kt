package com.menesdurak.e_ticaret_uygulamasi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCartProduct(cartProduct: CartProduct)

    @Query("DELETE FROM cart_table WHERE id = :cartProductId")
    suspend fun deleteCartProductWithId(cartProductId: Int)

    @Query("DELETE FROM cart_table")
    suspend fun deleteAllCartProducts()

    @Query("DELETE FROM cart_table WHERE isChecked = 1")
    suspend fun deleteAllCheckedCartProducts()

    @Query("SELECT * FROM cart_table ORDER BY id ASC")
    suspend fun getAllCartProducts(): List<CartProduct>

    @Query("UPDATE cart_table SET isChecked = :isCheckedStatus WHERE id = :cartProductId")
    suspend fun updateCartProductCheckedStatus(isCheckedStatus: Boolean, cartProductId: Int)

    @Query("UPDATE cart_table SET amount = :newAmount WHERE id = :cartProductId")
    suspend fun updateCartProductAmount(newAmount: Int, cartProductId: Int)

    @Query("UPDATE cart_table SET isChecked = 1 WHERE 1")
    suspend fun updateAllCartProductsToChecked()

    @Query("UPDATE cart_table SET isChecked = 0 WHERE 1")
    suspend fun updateAllCartProductsToNotChecked()
}