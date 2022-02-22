package com.example.currencyconverter.repository.remote

import com.example.currencyconverter.repository.model.Currency
import retrofit2.Call

class CurrencyRemoteRepository(private val currencyApi: CurrencyApi) {

    fun getConversion(to: String, from: String, amount: String): Call<Currency>
        = currencyApi.getConversion(to, from, amount)

    fun getBaseConversion(base: String, target: String) : Call<Currency> 
        = currencyApi.getBaseConversion(base, target)
    
}