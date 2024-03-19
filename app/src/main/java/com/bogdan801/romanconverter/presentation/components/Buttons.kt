package com.bogdan801.romanconverter.presentation.components

import android.graphics.BlurMaskFilter
import android.graphics.Typeface.NORMAL
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.util.shadowCustom

@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit ={}
) {
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .size(size)
            .shadowCustom(
                color = Color.Black.copy(alpha = 0.25f),
                blurRadius = 10.dp,
                shapeRadius = 21.dp
            )
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onTertiaryContainer,
                        MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    radius = with(density) { size.toPx() / 2f },
                    center = Offset(
                        x = with(density) { size.toPx() / 2f },
                        y = with(density) { size.toPx() / 2f }
                    )
                )
            )
            .clickable(onClick = onClick)

    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            icon()
        }
    }
}

