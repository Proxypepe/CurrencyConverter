package com.example.currencyconverter.domain

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencyconverter.repository.local.CurrencyRepository
import com.example.currencyconverter.repository.model.Currency
import com.example.currencyconverter.repository.remote.CurrencyRemoteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrencyViewModel(private val currencyRemoteRepository: CurrencyRemoteRepository?,
                        private val localRepository: CurrencyRepository?) : ViewModel() {

    var loading = MutableLiveData(false)

    private var currencyTo: String = "RUB"

    private var currencyFrom: String = "RUB"


    private val conversionResult = MutableLiveData(Currency(result="",
        documentation="", terms_of_use="", time_last_update_unix=0L, time_last_update_utc="",
        time_next_update_unix=0L, time_next_update_utc="",base_code="",
        target_code="", conversion_rate=0F, conversion_result=0F))

    fun getConversion(to: String, from: String, amount: String) : Currency? {
        loading.value = true
        currencyRemoteRepository?.getConversion(to, from, amount)?.enqueue(object : Callback<Currency>{
            override fun onResponse(call: Call<Currency>, response: Response<Currency>) {
                Log.d("Ok", "All ok $to $from")
                if(response.isSuccessful) {
                    val req = response.body()
                    req?.let {
                        Log.d("Value", "$it")
                        conversionResult.value = it
                        loading.value = false
                        Log.d("enaue", "$loading.value")
                    }
                    if (req == null) {
                        loading.value = false
                    }
                }
            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {
                Log.d("Error", "")
                loading.value = false
            }

        })
        return conversionResult.value
    }

    fun getConversionResult() = conversionResult
    fun setCurrencyTo(currency: String) { currencyTo = currency }
    fun getCurrencyTo() = currencyTo
    fun setCurrencyFrom(currency: String) { currencyFrom = currency }
    fun getCurrencyFrom() = currencyFrom

}


class CurrencyViewModelFactory(private val remoteRepository: CurrencyRemoteRepository,
        private val localRepository: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyViewModel(remoteRepository, localRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}