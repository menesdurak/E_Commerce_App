package com.menesdurak.e_ticaret_uygulamasi.data.remote.dto

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserInfo(
    var name: String? = "",
    var surName: String? = "",
    var address: String? = "",
    var phone: String? = ""
)