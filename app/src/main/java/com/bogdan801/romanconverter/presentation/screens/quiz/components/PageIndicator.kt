package com.bogdan801.romanconverter.presentation.screens.quiz.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun PageIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int = 2,
    value: Float = 0f,
    dotSize: Dp = 10.dp,
    dotColor: Color = MaterialTheme.colorScheme.onTertiary,
    paddingBetween: Dp = 10.dp,
    onPageClick: (id: Int) -> Unit = {}
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(paddingBetween)
    ) {
        repeat(pageCount) { index ->
            Box(modifier = Modifier
                .size(dotSize)
                .clip(CircleShape)
                .border(
                    1.dp,
                    color = dotColor,
                    CircleShape
                )
                .background(dotColor.copy(alpha = (1f - abs(index - value)).coerceIn(0f, 1f)))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        onPageClick(index)
                    }
                )
            )
        }
    }
}