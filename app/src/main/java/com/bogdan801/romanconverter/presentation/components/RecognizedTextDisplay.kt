package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.theme.displayGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

@Composable
fun RecognizedTextDisplay(
    modifier: Modifier = Modifier,
    recognizedText: String = ""
) {
    Box(
        modifier = modifier
            .aspectRatio(2f)
            .shadowCustom(
                color = Color.Black.copy(alpha = 0.2f),
                shapeRadius = 12.dp,
                blurRadius = 30.dp,
                offsetY = 10.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(displayGradientBrush()),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = recognizedText,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}