package com.menesdurak.e_ticaret_uygulamasi.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.FavoriteProduct

@Database(entities = [FavoriteProduct::class], version = 1)
abstract class ETicaretDatabase : RoomDatabase() {

    abstract fun getETicaretDao(): ETicaretDao

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