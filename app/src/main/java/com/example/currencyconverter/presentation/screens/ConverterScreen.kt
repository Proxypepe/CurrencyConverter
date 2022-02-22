package com.example.currencyconverter.presentation.screens


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.currencyconverter.domain.CurrencyViewModel


@Composable
fun ConverterScreen(navController: NavController, currencyViewModel: CurrencyViewModel) {
    val valueFrom   = remember { mutableStateOf(TextFieldValue()) }
    currencyViewModel.context = LocalContext.current
    val currencyLiveData = currencyViewModel.getConversionResult().observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        contentAlignment = Alignment.Center
    )
    {
         Column(
             Modifier
                 .fillMaxSize()
                 .padding(10.dp)
         ){
             Text(text = "You send", modifier = Modifier.padding(bottom = 5.dp))
             Row {
                OutlinedTextField(value = valueFrom.value, onValueChange = {
                    valueFrom.value = it},
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.padding(10.dp))
                Dropdown(currencyViewModel, "to")
             }
             Spacer(modifier = Modifier.padding(10.dp))
             Column(
                 modifier = Modifier.fillMaxWidth(),
                 verticalArrangement = Arrangement.Center,
                 horizontalAlignment = Alignment.CenterHorizontally
             ) {
                 Button(
                     onClick = {
                         currencyViewModel.getConversion(currencyViewModel.getCurrencyTo(),
                             currencyViewModel.getCurrencyFrom(), valueFrom.value.text)
                     }, colors = ButtonDefaults.textButtonColors(
                         backgroundColor = Color.White
                     )
                 ) {
                     Text("Convert  ")
                     Log.d("Loading",  "${currencyViewModel.loading}")
                     LoadProgress(currencyViewModel.loading)
                 }
             }
             Spacer(modifier = Modifier.padding(10.dp))
             Text(text = "They get", modifier = Modifier.padding(bottom = 5.dp).width(100.dp))
             Row {
                 Text(text=currencyLiveData.value?.conversion_result.toString(),
                     modifier = Modifier.width(280.dp))
                 Spacer(modifier = Modifier.padding(10.dp))
                 Dropdown(currencyViewModel, "from")
             }
         }
    }
}

@Composable
fun Dropdown(currencyViewModel: CurrencyViewModel, pos: String) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("RUB", "USD", "GBP")
    var selectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier
        .padding(bottom = 10.dp)
        .width(100.dp)
        .height(50.dp)){
        Text(
            items[selectedIndex],
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = { expanded = true })
                .padding(15.dp), textAlign = TextAlign.Center
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.wrapContentSize()
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(onClick = {
                    selectedIndex = index
                    expanded = false
                    if (pos == "to")
                    {
                        currencyViewModel.setCurrencyTo(s)
                    } else if (pos == "from") {
                        currencyViewModel.setCurrencyFrom(s)
                    }
                }){
                    Text(text = s)
                }
            }
        }// DropdownMenu
    }// Box
}// fun


@Preview(showBackground = true)
@Composable
fun ConverterScreenTest() {
    val navController = rememberNavController()
    val viewModel = CurrencyViewModel(null, null)
    ConverterScreen(navController, viewModel)
}