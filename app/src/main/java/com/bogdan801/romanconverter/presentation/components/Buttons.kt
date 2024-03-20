package com.bogdan801.romanconverter.presentation.components

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.presentation.theme.iconButtonGradientBrush
import com.bogdan801.romanconverter.presentation.theme.inputButtonGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

@Composable
fun SmallIconButton(
    modifier: Modifier = Modifier,
    size: Dp = 42.dp,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit ={}
) {
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
                brush = iconButtonGradientBrush(size)
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

@Composable
fun InputButton(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    isEnabled: Boolean = true,
    onClick: () -> Unit = {},
    label: String? = null,
    isBackspace: Boolean = false,
    customIcon: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()

    val shadowColor = animateColorAsState(
        targetValue = when {
            isEnabled && isPressed.value -> Color.Black.copy(alpha = 0.1f)
            isEnabled && !isPressed.value -> Color.Black.copy(alpha = 0.25f)
            else -> Color.Transparent
        },
        label = ""
    )
    val offsetY = animateDpAsState(
        targetValue = if (!isPressed.value) 4.dp else 2.dp,
        label = ""
    )
    val context = LocalContext.current
    Box(
        modifier = modifier
            .size(size)
            .shadowCustom(
                color = shadowColor.value,
                blurRadius = 14.dp,
                shapeRadius = size / 2,
                offsetY = offsetY.value
            )
            .clip(CircleShape)
            .background(inputButtonGradientBrush(size))
            .clickable(
                onClick = {
                    if (isEnabled) {
                        Toast
                            .makeText(context, label?:"backspace", Toast.LENGTH_SHORT)
                            .show()
                        onClick()
                    }
                },
                interactionSource = interactionSource,
                indication = null
            )

    ) {
        val scrim = animateColorAsState(
            targetValue = if (isPressed.value)
                            MaterialTheme.colorScheme.scrim
                          else Color.Transparent,
            label = ""
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isEnabled) scrim.value else MaterialTheme.colorScheme.surfaceBright),
            contentAlignment = Alignment.Center
        ){

            when {
                customIcon != null -> {
                    customIcon()
                }
                label != null -> {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
                isBackspace -> {
                    val primary = MaterialTheme.colorScheme.primary
                    val secondary = MaterialTheme.colorScheme.secondary
                    Icon(
                        modifier = Modifier
                            .graphicsLayer(alpha = 0.99f)
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    drawRect(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                primary, secondary, primary
                                            )
                                        ),
                                        blendMode = BlendMode.SrcAtop
                                    )
                                }
                            },
                        imageVector = Icons.AutoMirrored.Outlined.Backspace,
                        contentDescription = "backspace"
                    )
                }
            }
        }
    }
}
