package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.sizeIn
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
import androidx.compose.ui.graphics.Color
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
import com.bogdan801.romanconverter.presentation.components.ConvertDisplay
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ){
            ConvertDisplay(
                modifier = Modifier
                    .padding(top = 72.dp, start = 24.dp, end = 24.dp)
                    .fillMaxSize()
                    .sizeIn(
                        maxWidth = 400.dp,
                        maxHeight = 300.dp
                    ),
                romanValue = screenState.romanValue,
                arabicValue = screenState.arabicValue,
                type = screenState.type
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