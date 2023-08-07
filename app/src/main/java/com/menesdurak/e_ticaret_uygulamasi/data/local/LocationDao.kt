package com.menesdurak.e_ticaret_uygulamasi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.Location

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLocation(location: Location)

    @Query("DELETE FROM location_table WHERE id = :locationId")
    suspend fun deleteLocationWithId(locationId: Int)

    @Query("DELETE FROM location_table")
    suspend fun deleteAllLocations()

    @Query("SELECT * FROM location_table ORDER BY id ASC")
    suspend fun getAllLocations(): List<Location>
}