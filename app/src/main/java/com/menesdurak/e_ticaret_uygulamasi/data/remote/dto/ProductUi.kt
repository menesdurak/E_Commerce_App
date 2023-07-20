package com.menesdurak.e_ticaret_uygulamasi.data.remote.dto

data class ProductUi(
    val category: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    var isFavorite: Boolean = false,
    var isInCart: Boolean = false
)