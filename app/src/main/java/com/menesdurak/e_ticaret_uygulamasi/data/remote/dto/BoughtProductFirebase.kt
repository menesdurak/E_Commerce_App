package com.menesdurak.e_ticaret_uygulamasi.data.remote.dto

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class BoughtProductFirebase(
    var category: String? = "",
    var description: String? = "",
    var id: Int? = -1,
    var image: String? = "",
    var price: String? = "",
    var title: String? = "",
    var amount: Int? = -1,
    var isDelivered: Boolean? = false,
    var creditCardNumber: String? = "",
    var address: String? = "",
    var orderDate: String? = ""
)