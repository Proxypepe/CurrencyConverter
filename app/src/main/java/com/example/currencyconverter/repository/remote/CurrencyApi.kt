package com.example.currencyconverter.repository.remote

import com.example.currencyconverter.repository.model.Currency
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("pair/{to}/{from}/{amount}")
    fun getConversion(@Path("to") to: String, @Path("from") from: String, @Path("amount") amount: String) : Call<Currency>

    @GET("pair/{first}/{second}")
    fun getBaseConversion(@Path("first") first: String, @Path("second") second: String) : Call<Currency>
}