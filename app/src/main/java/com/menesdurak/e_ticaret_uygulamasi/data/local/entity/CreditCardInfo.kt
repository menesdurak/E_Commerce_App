package com.menesdurak.e_ticaret_uygulamasi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("credit_card_table")
data class CreditCardInfo (
    val number: String,
    val holderName: String,
    val expireMonth: String,
    val expireYear: String,
    val cvc: String,
    var isActive: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var id : Short = 0
}