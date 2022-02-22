package com.example.currencyconverter.repository.local.dao

import androidx.room.*
import com.example.currencyconverter.repository.local.entity.CurrencyDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies WHERE name = :base or name = :target")
    fun getCurrencies(base: String, target: String) : Flow<List<CurrencyDTO?>>

    @Query("SELECT * FROM currencies WHERE name = :base")
    fun getCurrency(base: String) : Flow<CurrencyDTO?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrency(currencyDto: CurrencyDTO)

    @Update
    suspend fun updateCurrency(currencyDto: CurrencyDTO)

}