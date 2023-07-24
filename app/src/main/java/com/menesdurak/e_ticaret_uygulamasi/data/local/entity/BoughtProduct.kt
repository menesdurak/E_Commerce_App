package com.menesdurak.e_ticaret_uygulamasi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class BoughtProduct(
    val category: String,
    val description: String,
    @PrimaryKey
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    var amount: Int,
    var isDelivered: Boolean = false,
    val creditCardNumber: String,
    val address: String,
    val orderDate: String
)