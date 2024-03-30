package com.bogdan801.romanconverter.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.data.util.convertSecondsToTimeString
import com.bogdan801.romanconverter.presentation.theme.counterGradientBrush

@Composable
fun CounterCell(
    modifier: Modifier = Modifier,
    value: String,
    borderWidth: Dp = 0.1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.3f),
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontSize: TextUnit = textStyle.fontSize,
    rollUp: Boolean = true
) {
    AnimatedContent(
        modifier = modifier
            .background(counterGradientBrush())
            .border(width = borderWidth, color = borderColor),
        targetState = value,
        transitionSpec = {
            if(rollUp){
                slideInVertically(
                    animationSpec = tween(easing = LinearEasing),
                    initialOffsetY = { it }
                ) togetherWith slideOutVertically(
                    animationSpec = tween(easing = LinearEasing),
                    targetOffsetY = { -it }
                )
            }
            else {
                slideInVertically(
                    animationSpec = tween(easing = LinearEasing),
                    initialOffsetY = { -it }
                ) togetherWith slideOutVertically(
                    animationSpec = tween(easing = LinearEasing),
                    targetOffsetY = { it }
                )
            }
        },
        label = ""
    ) { animatedValue ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(
                modifier = Modifier.offset(y = 1.5.dp),
                text = animatedValue,
                color = textColor,
                style = textStyle,
                fontSize = fontSize,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ValueCounter(
    modifier: Modifier,
    digitCount: Int = 5,
    value: Int = 0,
    prevValue: Int = 0,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize
) {
    val stringValue = value.toString()
    Row(modifier = modifier
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        for (i in 0 until digitCount){
            val displayI = i - (digitCount - stringValue.length)
            val prevI = i - (digitCount - prevValue.toString().length)
            val prevDigit = if(prevI < 0) "0" else prevValue.toString()[prevI].toString()
            val displayDigit = if(displayI < 0) "0" else stringValue[displayI].toString()
            val targetValue = if(prevDigit > displayDigit) displayDigit.toFloat() + 10f
            else displayDigit.toFloat()
            val animatable = remember { Animatable(prevDigit.toFloat()) }
            LaunchedEffect(key1 = targetValue) {
                animatable.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(
                        durationMillis = 800,
                        easing = LinearEasing
                    )
                )
                animatable.snapTo(displayDigit.toFloat())
            }
            CounterCell(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                value = if(digitCount>1)(animatable.value.toInt() % 10).toString() else value.toString(),
                rollUp = false,
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun TimeCounter(
    modifier: Modifier = Modifier,
    value: Int,
    fontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize
) {
    val stringValue = convertSecondsToTimeString(value)
    Row(modifier = modifier
        .background(counterGradientBrush())
        .border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.onTertiary
        )
    ) {
        val cellColor = if(value in 1..9) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onTertiary
        CounterCell(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            value = stringValue[0].toString(),
            textColor = cellColor,
            fontSize = fontSize
        )
        CounterCell(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            value = stringValue[1].toString(),
            textColor = cellColor,
            fontSize = fontSize
        )
        Text(
            modifier = Modifier.width(12.dp).offset(y = (-1).dp),
            text = ":",
            textAlign = TextAlign.Center,
            color = cellColor,
            style = MaterialTheme.typography.titleLarge
        )
        CounterCell(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            value = stringValue[3].toString(),
            textColor = cellColor,
            fontSize = fontSize
        )
        CounterCell(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            value = stringValue[4].toString(),
            textColor = cellColor,
            fontSize = fontSize
        )
    }
}