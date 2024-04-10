package com.bogdan801.romanconverter.presentation.theme

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.bogdan801.romanconverter.R
import com.bogdan801.util_library.intSettings

private val LightColorScheme = lightColorScheme(
    primary = gold900,
    secondary = gold200,
    tertiary = Color.Black.copy(0.05f),
    onPrimary = gray0,
    onSecondary = gray110,
    onTertiary = gray220,
    primaryContainer = gray670,
    secondaryContainer = gray850,
    tertiaryContainer = gray920,
    onPrimaryContainer = gray950,
    onSecondaryContainer = gray980,
    onTertiaryContainer = gray1000,
    surface = gray220,
    surfaceVariant = gray110,
    onSurface = gold100,
    error = red,
    background = Color.White,
    outline = gold800,
    outlineVariant = gray850,
    scrim = Color.Black.copy(0.08f),
    surfaceBright = Color.White.copy(alpha = 0.6f)
)

private val DarkColorScheme = darkColorScheme(
    primary = gold900,
    secondary = gold200,
    tertiary = Color.Black.copy(0.40f),
    onPrimary = gray1000,
    onSecondary = gray180,
    onTertiary = gray850,
    primaryContainer = gray670,
    secondaryContainer = gray100,
    tertiaryContainer = gray110,
    onPrimaryContainer = gray130,
    onSecondaryContainer = gray230,
    onTertiaryContainer = gray250,
    surface = gray180,
    surfaceVariant = gray180,
    onSurface = gold100,
    error = red,
    background = gray0,
    outline = gold800,
    outlineVariant = gray490,
    scrim = Color.White.copy(0.16f),
    surfaceBright = Color.Black.copy(alpha = 0.5f)
)

@Composable
fun iconButtonGradientBrush(size: Dp = 42.dp): Brush {
    val density = LocalDensity.current
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    return remember(size, onTertiaryContainer, onPrimaryContainer) {
        Brush.radialGradient(
            colors = listOf(onTertiaryContainer, onPrimaryContainer),
            radius = with(density) { size.toPx() / 2f },
            center = Offset(
                x = with(density) { size.toPx() / 2f },
                y = with(density) { size.toPx() / 2f }
            )
        )
    }
}

@Composable
fun inputButtonGradientBrush(size: Dp = 42.dp): Brush {
    val density = LocalDensity.current
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val secondaryContainer = MaterialTheme.colorScheme.secondaryContainer
    return remember(size,onPrimaryContainer, secondaryContainer, onSecondaryContainer, onTertiaryContainer) {
        Brush.radialGradient(
            colorStops = arrayOf(
                0f to onPrimaryContainer,
                0.6f to onSecondaryContainer,
                0.76f to onTertiaryContainer,
                0.84f to onSecondaryContainer,
                1f to secondaryContainer

            ),
            radius = with(density) { size.toPx() / 2f },
            center = Offset(
                x = with(density) { size.toPx() / 2f },
                y = with(density) { ((size / 2f)-2.dp).toPx() }
            )
        )
    }
}

@Composable
fun actionButtonGradientBrush(): Brush {
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    return remember(onTertiaryContainer, tertiaryContainer) {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0f to onTertiaryContainer,
                0.33f to onTertiaryContainer,
                1f to tertiaryContainer

            )
        )
    }
}

@Composable
fun navbarGradientBrush(): Brush {
    val surface = MaterialTheme.colorScheme.surface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    return remember(surface, surfaceVariant) {
        Brush.linearGradient(
            colors = listOf(surface, surfaceVariant)
        )
    }
}

@Composable
fun counterGradientBrush(): Brush {
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    return remember(tertiaryContainer, onSecondaryContainer, onTertiaryContainer) {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0f to tertiaryContainer,
                0.25f to onSecondaryContainer,
                0.5f to onTertiaryContainer,
                0.75f to onSecondaryContainer,
                1f to tertiaryContainer
            )
        )
    }
}

@Composable
fun dialogBoxGradientBrush(): Brush {
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val tertiaryContainer = MaterialTheme.colorScheme.tertiaryContainer
    return remember(tertiaryContainer, onTertiaryContainer) {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0f to onTertiaryContainer,
                0.4f to onTertiaryContainer,
                1f to tertiaryContainer
            )
        )
    }
}

@Composable
fun displayGradientBrush(): Brush {
    val onTertiaryContainer = MaterialTheme.colorScheme.onTertiaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    return remember(onPrimaryContainer, onTertiaryContainer) {
        Brush.verticalGradient(
            colors = listOf(
                onTertiaryContainer,
                onPrimaryContainer
            )
        )
    }
}

@Composable
fun quizSelectorGradientBrush(): Brush {
    val onSecondaryContainer = MaterialTheme.colorScheme.onSecondaryContainer
    val onPrimaryContainer = MaterialTheme.colorScheme.onPrimaryContainer
    return remember(onSecondaryContainer, onPrimaryContainer) {
        Brush.verticalGradient(
            colorStops = arrayOf(
                0f to onSecondaryContainer,
                0.5f to onSecondaryContainer,
                1f to onPrimaryContainer
            )
        )
    }
}

@Composable
fun RomanCalculatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val view = LocalView.current

    //observing theme change
    val currentTheme = context.intSettings["theme"].collectAsState(initial = if(darkTheme) 1 else 0)
    val colorScheme by remember {
        derivedStateOf {
            when(currentTheme.value){
                0 -> LightColorScheme
                1 -> DarkColorScheme
                else -> if(darkTheme) DarkColorScheme else LightColorScheme
            }
        }
    }

    //updating bars colors
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = colorScheme == LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                //background texture
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(
                        id = when(currentTheme.value){
                            0 -> R.drawable.white_texture
                            1 -> R.drawable.black_texture
                            else -> {
                                if (!darkTheme) R.drawable.white_texture
                                else R.drawable.black_texture
                            }
                        }
                    ),
                    contentDescription = "background",
                    contentScale = ContentScale.Crop
                )

                //content
                Column(
                    modifier = Modifier.fillMaxSize()
                ){
                    content()
                }
            }
        }
    )
}