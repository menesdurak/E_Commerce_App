package com.menesdurak.e_ticaret_uygulamasi.di

import com.menesdurak.e_ticaret_uygulamasi.common.Constants.BASE_URL
import com.menesdurak.e_ticaret_uygulamasi.data.remote.api.ETicaretApi
import com.menesdurak.e_ticaret_uygulamasi.data.repository.RemoteRepositoryImpl
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.RemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideETicaretApi(): ETicaretApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ETicaretApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteRepository(api: ETicaretApi): RemoteRepository {
        return RemoteRepositoryImpl(api)
    }
}