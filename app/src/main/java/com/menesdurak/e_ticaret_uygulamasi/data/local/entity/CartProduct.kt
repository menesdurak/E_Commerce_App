package com.menesdurak.e_ticaret_uygulamasi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartProduct(
    val category: String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    var amount: Int,
    var isChecked: Boolean = false
)