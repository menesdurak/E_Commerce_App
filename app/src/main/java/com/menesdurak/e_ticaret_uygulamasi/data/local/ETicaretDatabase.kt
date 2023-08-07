package com.menesdurak.e_ticaret_uygulamasi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CartProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location

@Database(
    entities = [FavoriteProduct::class, CartProduct::class, CreditCardInfo::class, Location::class],
    version = 1
)
abstract class ETicaretDatabase : RoomDatabase() {

    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getCartDao(): CartDao
    abstract fun getCreditCardDao(): CreditCardDao
    abstract fun getLocationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: ETicaretDatabase? = null

        fun getDatabase(context: Context): ETicaretDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ETicaretDatabase::class.java,
                    "e_ticaret_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}