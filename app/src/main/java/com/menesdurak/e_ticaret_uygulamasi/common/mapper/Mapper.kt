package com.menesdurak.e_ticaret_uygulamasi.common.mapper

interface Mapper<I,O>{
    fun map(input:I):O
}