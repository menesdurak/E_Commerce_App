package com.menesdurak.e_ticaret_uygulamasi.di

import android.app.Application
import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDatabase
import com.menesdurak.e_ticaret_uygulamasi.data.repository.LocalRepositoryImpl
import com.menesdurak.e_ticaret_uygulamasi.domain.repository.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun getDeezerDatabase(context: Application): ETicaretDatabase {
        return ETicaretDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun getFavoriteProductDao(favoriteProductDatabase: ETicaretDatabase): ETicaretDao {
        return favoriteProductDatabase.getETicaretDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(dao: ETicaretDao): LocalRepository {
        return LocalRepositoryImpl(dao)
    }
}