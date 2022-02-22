package com.example.currencyconverter.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.currencyconverter.repository.local.entity.CurrencyDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencies WHERE name = :name")
    fun getCurrency(name: String) : Flow<CurrencyDTO>

    @Insert
    suspend fun insertCurrency(currencyDto: CurrencyDTO) : Flow<CurrencyDTO>

    @Update
    suspend fun updateCurrency(currencyDto: CurrencyDTO) : Flow<CurrencyDTO>

}