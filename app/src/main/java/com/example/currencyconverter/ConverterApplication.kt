package com.example.currencyconverter

import android.app.Application
import com.example.currencyconverter.repository.remote.CurrencyApi
import com.example.currencyconverter.repository.remote.CurrencyRemoteRepository
import com.example.currencyconverter.repository.remote.RetrofitFactory


class ConverterApplication : Application() {
    private val currencyApi by lazy {
        RetrofitFactory().getInstance<CurrencyApi>("https://v6.exchangerate-api.com/v6/85b395765bd70181489a4d52/")
    }
    val currencyRemoteRepository by lazy {
        CurrencyRemoteRepository(currencyApi)
    }
}