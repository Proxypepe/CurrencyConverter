package com.example.currencyconverter.domain

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.currencyconverter.presentation.activity.MainActivity
import com.example.currencyconverter.repository.local.CurrencyRepository
import com.example.currencyconverter.repository.local.entity.CurrencyDTO
import com.example.currencyconverter.repository.model.Currency
import com.example.currencyconverter.repository.remote.CurrencyRemoteRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CurrencyViewModel(private val currencyRemoteRepository: CurrencyRemoteRepository?,
                        private val localRepository: CurrencyRepository?) : ViewModel() {
    private val targetCode: String = "EUR"

    @SuppressLint("StaticFieldLeak")
    var context: Context? = null

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
                        saveCurrency(to)
                        saveCurrency(from)
                        Log.d("enaue", "$loading.value")
                    }
                    if (req == null) {
                        loading.value = false
                    }
                }
            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {
                Log.d("Error", "")

                var res = 0F
                localRepository?.getCurrencies(to, from)?.asLiveData()?.observe(context as MainActivity){
                    Log.d("All ok with data", it.toString())
                    if (it.size == 2){
                        try {
                            if (it[0]?.name == to && it[0]?.price != 0f && it[1]?.price != 0f)
                            {
                                res = calculate(amount.toFloat(), it[0]?.price!!, it[1]?.price!!)
                                Log.d("first", "$res")
                            } else {
                                res = calculate(amount.toFloat(), it[1]?.price!!, it[0]?.price!!)
                                Log.d("second", "$res")
                            }
                            conversionResult.value = Currency(result="",
                                documentation="", terms_of_use="", time_last_update_unix=0L, time_last_update_utc="",
                                time_next_update_unix=0L, time_next_update_utc="",base_code=to,
                                target_code=from, conversion_rate=0F, conversion_result=res)
                            loading.value = false
                        }
                        catch (e: Exception){
                            Log.d("Exception", e.toString())
                            loading.value = false
                        }
                    } else {
                        Toast.makeText(context, "Don't have this currency", Toast.LENGTH_LONG).show()
                        loading.value = false
                    }
                }
            }
        })
        return conversionResult.value
    }

    private fun calculate(amount: Float, first: Float, second: Float): Float = amount * first / second

    private fun saveCurrency(baseCode: String) {
        currencyRemoteRepository?.getBaseConversion(baseCode, targetCode)?.enqueue(object : Callback<Currency>{
            override fun onResponse(call: Call<Currency>, response: Response<Currency>) {
                if(response.isSuccessful) {
                    val req = response.body()
                    req?.let {
                        val currencyDto = CurrencyDTO(name = it.base_code, price = it.conversion_rate)
                        insert(currencyDto)
                    }
                }
            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {

            }
        })

    }

    private fun insert(currency: CurrencyDTO) = viewModelScope.launch {
        localRepository?.insertCurrency(currency)
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