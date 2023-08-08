package com.menesdurak.e_ticaret_uygulamasi.data.remote.dto


data class BoughtProduct(
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
    var orderDate: String? = "",
    var orderNumber: String? = ""
)