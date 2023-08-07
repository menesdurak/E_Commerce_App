package com.menesdurak.e_ticaret_uygulamasi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("location_table")
data class Location (
    val latitude: Double,
    val longitude: Double,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}