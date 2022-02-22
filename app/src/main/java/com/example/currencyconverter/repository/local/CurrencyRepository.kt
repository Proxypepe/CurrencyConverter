package com.example.currencyconverter.repository.local

import androidx.annotation.WorkerThread
import com.example.currencyconverter.repository.local.dao.CurrencyDao
import com.example.currencyconverter.repository.local.entity.CurrencyDTO
import kotlinx.coroutines.flow.Flow

class CurrencyRepository (private val currencyDao: CurrencyDao){

    fun getCurrencies(base: String, target: String): Flow<List<CurrencyDTO?>> = currencyDao.getCurrencies(base, target)

    @WorkerThread
    suspend fun insertCurrency(currency: CurrencyDTO){
        currencyDao.insertCurrency(currency)
    }
}