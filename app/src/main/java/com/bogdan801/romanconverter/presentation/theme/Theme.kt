package com.bogdan801.romanconverter.presentation.theme

import android.app.Activity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.core.view.drawToBitmap
import com.bogdan801.romanconverter.R
import com.bogdan801.util_library.intSettings
import com.bogdan801.util_library.stringSettings
import kotlin.math.sqrt

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
    surface = gray110,
    surfaceVariant = gray220,
    onSurface = gold100,
    error = red,
    background = Color.White,
    outline = gold800,
    outlineVariant = Color.Black,
    scrim = Color.Black.copy(0.35f)
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
    surface = gray150,
    surfaceVariant = gray180,
    onSurface = gold100,
    error = red,
    background = gray0,
    outline = gold800,
    outlineVariant = gray490,
    scrim = Color.Black.copy(0.35f)
)

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