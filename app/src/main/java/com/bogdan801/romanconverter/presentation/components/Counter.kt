package com.bogdan801.romanconverter.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.theme.counterGradientBrush

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CounterCell(
    modifier: Modifier = Modifier,
    value: String,
    borderWidth: Dp = 0.1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.3f),
    textColor: Color = MaterialTheme.colorScheme.onTertiary,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium
) {

    AnimatedContent(
        modifier = modifier
            .background(counterGradientBrush())
            .border(width = borderWidth, color = borderColor),
        targetState = value,
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(easing = LinearEasing),
                initialOffsetY = { it }
            ) togetherWith slideOutVertically(
                animationSpec = tween(easing = LinearEasing),
                targetOffsetY = { -it }
            )
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
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ValueCounter(
    modifier: Modifier,
    digitCount: Int = 5,
    value: Int = 0
) {
    val stringValue = value.toString()
    Row(modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onTertiary
            )
    ) {
        for (i in 0 until digitCount){
            CounterCell(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                value = "4"
            )
        }
    }
}