package com.menesdurak.e_ticaret_uygulamasi.di

import android.app.Application
import com.menesdurak.e_ticaret_uygulamasi.data.local.CartDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.CreditCardDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.FavoriteDao
import com.menesdurak.e_ticaret_uygulamasi.data.local.ETicaretDatabase
import com.menesdurak.e_ticaret_uygulamasi.data.local.LocationDao
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
    fun getETicaretDatabase(context: Application): ETicaretDatabase {
        return ETicaretDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun getFavoriteDao(eTicaretDatabase: ETicaretDatabase): FavoriteDao {
        return eTicaretDatabase.getFavoriteDao()
    }

    @Provides
    @Singleton
    fun getCartDao(eTicaretDatabase: ETicaretDatabase): CartDao {
        return eTicaretDatabase.getCartDao()
    }

    @Provides
    @Singleton
    fun getCreditCardDao(eTicaretDatabase: ETicaretDatabase): CreditCardDao {
        return eTicaretDatabase.getCreditCardDao()
    }

    @Provides
    @Singleton
    fun getLocationDao(eTicaretDatabase: ETicaretDatabase): LocationDao {
        return eTicaretDatabase.getLocationDao()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(
        favoriteDao: FavoriteDao,
        cartDao: CartDao,
        creditCardDao: CreditCardDao,
        locationDao: LocationDao
    ): LocalRepository {
        return LocalRepositoryImpl(favoriteDao, cartDao, creditCardDao, locationDao)
    }
}