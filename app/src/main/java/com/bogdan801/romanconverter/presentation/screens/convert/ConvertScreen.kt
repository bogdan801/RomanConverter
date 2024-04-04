package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.presentation.components.InputKeyboard
import com.bogdan801.romanconverter.presentation.components.InputKeyboardType
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel

@Composable
fun ConvertScreen(
    navController: NavHostController,
    viewModel: ConvertViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .padding(top = 72.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.TopCenter
        ){
            val width = maxWidth
            val height = maxHeight
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Icon(
                    modifier = Modifier.size(height * 0.22f),
                    painter = painterResource(id = R.drawable.ornament_main_display_left),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
                Icon(
                    modifier = Modifier.size(height * 0.22f),
                    painter = painterResource(id = R.drawable.ornament_main_display_right),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }


            val arabicFirstColor = if(screenState.type != InputKeyboardType.Arabic)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onTertiary

            val arabicSecondColor = if(screenState.type != InputKeyboardType.Arabic)
                MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.onTertiary

            val arabicFontSize = if(screenState.type == InputKeyboardType.Arabic)
                MaterialTheme.typography.displayLarge.fontSize.value
            else MaterialTheme.typography.displaySmall.fontSize.value

            val baseOffset by animateDpAsState(
                targetValue = if(screenState.type == InputKeyboardType.Arabic) height * 0.30f
                else height * 0.55f,
                label = ""
            )

            Text(
                modifier = Modifier
                    .offset{
                        IntOffset(
                            x = 0,
                            y = with(this) {baseOffset.toPx().toInt()},
                        )
                    }
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        arabicFirstColor, arabicSecondColor, arabicFirstColor
                                    )
                                ),
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    },
                text = screenState.arabicValue.ifBlank { "0" },
                fontSize = arabicFontSize.sp,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )


            val romanFirstColor = if(screenState.type != InputKeyboardType.Roman)
                MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onTertiary

            val romanSecondColor = if(screenState.type != InputKeyboardType.Roman)
                MaterialTheme.colorScheme.secondary
            else MaterialTheme.colorScheme.onTertiary

            val romanFontSize = if(screenState.type == InputKeyboardType.Roman)
                MaterialTheme.typography.displayLarge.fontSize.value
            else MaterialTheme.typography.displaySmall.fontSize.value


            Text(
                modifier = Modifier
                    .offset{
                        IntOffset(
                            x = 0,
                            y = with(this) { ((height * 0.3f) + ((height * 0.55f) - baseOffset)).toPx().toInt()},
                        )
                    }
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        romanFirstColor, romanSecondColor, romanFirstColor
                                    )
                                ),
                                blendMode = BlendMode.SrcAtop
                            )
                        }
                    },
                text = screenState.romanValue.ifBlank { "O" },
                fontSize = romanFontSize.sp,
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Icon(
                modifier = Modifier
                    .size(width, height = width * (25f / 328f) * 1.5f)
                    .align(Alignment.BottomCenter),
                painter = painterResource(id = R.drawable.ornamentmain_display_bottom),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.2f),
            contentAlignment = Alignment.Center
        ){
            InputKeyboard(
                romanValue = screenState.romanValue,
                onRomanValueChange = {
                    viewModel.setRomanValue(it)
                    viewModel.setArabicValue(convertRomanToArabic(it))
                },
                arabicValue = screenState.arabicValue,
                onArabicValueChange = {
                    viewModel.setArabicValue(it)
                    viewModel.setRomanValue(convertArabicToRoman(it))
                },
                type = screenState.type,
                onTypeChange = {
                    viewModel.setKeyboardType(it)
                },
                onClear = {
                    viewModel.setRomanValue("")
                    viewModel.setArabicValue("")
                }
            )
        }
    }
}