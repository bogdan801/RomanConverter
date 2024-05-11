package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.romanconverter.domain.model.QuizType
import com.bogdan801.romanconverter.presentation.theme.gray670
import com.bogdan801.romanconverter.presentation.theme.gray850
import com.bogdan801.romanconverter.presentation.theme.libreBodoniFontFamily
import com.bogdan801.romanconverter.presentation.theme.quizSelectorGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

@Composable
fun QuizTypeSelectorCard(
    modifier: Modifier = Modifier,
    type: QuizType = QuizType.GuessRoman,
    isSelected: Boolean = false,
    isSmall: Boolean = false,
    onSelected: () -> Unit = {}
) {
    val shadowAlpha by animateFloatAsState(
        targetValue = if (isSelected) 0.10f else 0f,
        label = ""
    )
    Surface(
        modifier = modifier
            .shadowCustom(
                color = Color.Black.copy(alpha = shadowAlpha),
                shapeRadius = 16.dp,
                blurRadius = 16.dp,
                offsetY = 4.dp
            ),
        color = Color.Transparent,
        shape = RoundedCornerShape(if(!isSmall) 16.dp else 8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if(isSelected) MaterialTheme.colorScheme.outline
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(quizSelectorGradientBrush())
                .clickable(onClick = onSelected)
        ){
            val aspectRatio = maxWidth/maxHeight
            val textBrush = if(isSelected){
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to MaterialTheme.colorScheme.primary,
                        0.5f to MaterialTheme.colorScheme.secondary,
                        1f to MaterialTheme.colorScheme.primary,
                    )
                )
            }
            else {
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to gray670,
                        0.5f to gray850,
                        1f to gray670,
                    )
                )
            }
            val textLabel = remember {
                if(!isSmall){
                    buildAnnotatedString {
                        when (type) {
                            QuizType.GuessRoman -> {
                                withStyle(style = SpanStyle(fontSize = 64.sp)) { append("X") }
                                withStyle(style = SpanStyle(fontSize = 46.sp)) { append("V") }
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("I") }
                            }
                            QuizType.GuessArabic -> {
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("1") }
                                withStyle(style = SpanStyle(fontSize = 46.sp)) { append("2") }
                                withStyle(style = SpanStyle(fontSize = 64.sp)) { append("3") }
                            }
                            QuizType.GuessBoth -> {
                                withStyle(style = SpanStyle(fontSize = 64.sp)) { append("V") }
                                withStyle(style = SpanStyle(fontSize = 46.sp)) { append("&") }
                                withStyle(style = SpanStyle(fontSize = 64.sp)) { append("5") }
                            }
                        }
                    }
                }
                else {
                    buildAnnotatedString {
                        when (type) {
                            QuizType.GuessRoman -> {
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("X") }
                                withStyle(style = SpanStyle(fontSize = 26.sp)) { append("V") }
                                withStyle(style = SpanStyle(fontSize = 20.sp)) { append("I") }
                            }
                            QuizType.GuessArabic -> {
                                withStyle(style = SpanStyle(fontSize = 20.sp)) { append("1") }
                                withStyle(style = SpanStyle(fontSize = 26.sp)) { append("2") }
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("3") }
                            }
                            QuizType.GuessBoth -> {
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("V") }
                                withStyle(style = SpanStyle(fontSize = 26.sp)) { append("&") }
                                withStyle(style = SpanStyle(fontSize = 38.sp)) { append("5") }
                            }
                        }
                    }
                }
            }

            val title = remember {
                when (type) {
                    QuizType.GuessRoman -> "Guess the\nRoman  numerals"
                    QuizType.GuessArabic -> "Guess the\nArabic  numerals"
                    QuizType.GuessBoth -> "Guess both"
                }
            }
            if(aspectRatio < 2f){
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = if(!isSmall) 16.dp else 10.dp,
                            vertical = if(!isSmall) 12.dp else 8.dp
                        )
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        modifier = Modifier.offset(y = 6.dp),
                        text = textLabel,
                        style = TextStyle(fontFamily = libreBodoniFontFamily, brush = textBrush)
                    )
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontSize = if(!isSmall) 14.sp else 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                }
            }
            else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .align(Alignment.Center),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Text(
                        text = textLabel,
                        style = TextStyle(fontFamily = libreBodoniFontFamily, brush = textBrush)
                    )
                }
            }
        }
    }
}