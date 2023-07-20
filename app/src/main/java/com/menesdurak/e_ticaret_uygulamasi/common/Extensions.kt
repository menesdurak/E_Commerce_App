package com.menesdurak.e_ticaret_uygulamasi.common

infix fun Float.round(decimals: Int): Float {
    var multiplier = 1
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
}