package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.theme.quizSelectorGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

enum class QuizType {
    GuessRoman,
    GuessArabic,
    GuessBoth
}

@Composable
fun QuizTypeSelector(
    modifier: Modifier = Modifier,
    type: QuizType = QuizType.GuessRoman,
    isSelected: Boolean = false,
    onSelected: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(16.dp),
        shadowElevation = if(isSelected) 6.dp else 0.dp,
        border = BorderStroke(
            width = 1.dp,
            color = if(isSelected) MaterialTheme.colorScheme.outline
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = quizSelectorGradientBrush()
                )
        )
    }
}