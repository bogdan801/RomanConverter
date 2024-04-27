package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.domain.model.QuizType

@Composable
fun QuizDisplay(
    modifier: Modifier = Modifier,
    currentType: QuizType = QuizType.GuessRoman,
    numberToGuess: String = "",
    inputValue: String = "",
    count: Int = 0,
    time: Int = 60,
    score: Int = 0,
    onSuccess: () -> Unit = {}
) {
    LaunchedEffect(key1 = inputValue) {
        when(currentType){
            QuizType.GuessRoman -> {
                if(convertArabicToRoman(inputValue) == numberToGuess) onSuccess()
            }
            QuizType.GuessArabic -> {
                val a = convertRomanToArabic(inputValue)
                val b = numberToGuess
                if(a == b) onSuccess()
            }
            QuizType.GuessBoth -> {}
        }

    }
    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(300.dp),
        contentAlignment = Alignment.Center
    ){
        val width = maxWidth
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = numberToGuess,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = inputValue,
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = count.toString(),
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}