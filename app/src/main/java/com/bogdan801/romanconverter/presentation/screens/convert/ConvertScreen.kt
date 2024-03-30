package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.presentation.components.ActionButton
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
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = screenState.arabicValue.ifBlank { "0" },
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onTertiary
            )
            Spacer(modifier = Modifier.height(48.dp))

            val primary = MaterialTheme.colorScheme.primary
            val secondary = MaterialTheme.colorScheme.secondary
            Text(
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
                text = screenState.romanValue.ifBlank { "O" },
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(48.dp))

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