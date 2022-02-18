package com.example.currencyconverter.presentation.screens


import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData


@Composable
fun LoadProgress(
    isDisplayed: MutableLiveData<Boolean>,
) {
    val loading = isDisplayed.observeAsState()
    if (loading.value == true) {
        Row (
//            modifier = Modifier
//            .fillMaxSize()
//            .padding(50.dp),
            horizontalArrangement = Arrangement.Center
        ){
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = Color.Green,
    stokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0

    ) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercentage = animateFloatAsState(
        targetValue = if(animationPlayed) percentage else 0f,
        animationSpec =  tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    var navigate1=  mutableStateOf(false)



    LaunchedEffect(key1 = true){
        animationPlayed = true
    }

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ){
        Canvas(modifier = Modifier.size(radius * 2f)) {
            drawArc(
                color = color,
                -90f,
                360 * curPercentage.value,
                useCenter = false,
                style = Stroke(stokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = (curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )

    }
}