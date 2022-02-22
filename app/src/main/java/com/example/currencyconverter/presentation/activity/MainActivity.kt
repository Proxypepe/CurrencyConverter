package com.example.currencyconverter.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Scaffold
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.ConverterApplication
import com.example.currencyconverter.domain.CurrencyViewModel
import com.example.currencyconverter.domain.CurrencyViewModelFactory
import com.example.currencyconverter.presentation.screens.ConverterScreen
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    private val currencyViewModel: CurrencyViewModel by viewModels {
        CurrencyViewModelFactory((application as ConverterApplication).currencyRemoteRepository,
        (application as ConverterApplication).localRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CurrencyConverterTheme {
                // A surface container using the 'background' color from the theme
                Scaffold {
                    ConverterScreen(navController, currencyViewModel)
                }
            }
        }
    }
}
