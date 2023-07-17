package com.menesdurak.e_ticaret_uygulamasi.domain.repository

interface RemoteRepository {

    suspend fun getAllCategories(): List<String>
}