package com.menesdurak.e_ticaret_uygulamasi.data.remote.api.map

import com.menesdurak.e_ticaret_uygulamasi.data.remote.dto.map.DirectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Eray BULUT on 13.08.2023
 * eraybulutlar@gmail.com
 */

interface ApiService {

    @GET("maps/api/directions/json")
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") apiKey: String
    ): Call<DirectionResponse>

}