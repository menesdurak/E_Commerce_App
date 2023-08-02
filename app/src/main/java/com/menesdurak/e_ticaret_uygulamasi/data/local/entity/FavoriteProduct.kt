package com.menesdurak.e_ticaret_uygulamasi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "e_ticaret_table")
data class FavoriteProduct(
    val category: String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    val whenFavorite: String,
    var isInCart: Boolean = false,
    val rating: Double,
    val ratingCount: Int
)
