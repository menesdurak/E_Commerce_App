package com.menesdurak.e_ticaret_uygulamasi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.menesdurak.e_ticaret_uygulamasi.data.local.entity.CreditCardInfo

@Dao
interface CreditCardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCreditCard(creditCardInfo: CreditCardInfo)

    @Query("DELETE FROM credit_card_table WHERE id = :creditCardId")
    suspend fun deleteCreditCardWithId(creditCardId: Short)

    @Query("DELETE FROM credit_card_table")
    suspend fun deleteAllCreditCards()

    @Query("SELECT * FROM credit_card_table ORDER BY id ASC")
    suspend fun getAllCreditCards(): List<CreditCardInfo>

    @Query("UPDATE credit_card_table SET isActive = :isActive WHERE id = :creditCardId")
    suspend fun updateCreditCardActiveStatus(isActive: Boolean, creditCardId: Short)
}