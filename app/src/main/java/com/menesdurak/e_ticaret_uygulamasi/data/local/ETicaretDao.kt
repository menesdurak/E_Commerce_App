package com.menesdurak.e_ticaret_uygulamasi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct

@Dao
interface ETicaretDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavoriteProduct(favoriteProduct: FavoriteProduct)

    @Query("DELETE FROM e_ticaret_table WHERE id = :favoriteProductId")
    suspend fun deleteFavoriteProductWithId(favoriteProductId: Int)

    @Query("SELECT * FROM e_ticaret_table ORDER BY id ASC")
    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>
}