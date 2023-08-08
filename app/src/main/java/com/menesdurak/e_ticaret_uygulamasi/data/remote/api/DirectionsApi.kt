package com.menesdurak.e_ticaret_uygulamasi.data.remote.api

import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.DirectionsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsApi {

    @GET("maps/api/directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionsResponse>
}