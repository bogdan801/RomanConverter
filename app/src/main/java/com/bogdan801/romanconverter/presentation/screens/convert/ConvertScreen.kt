package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.presentation.screens.convert.components.ConvertDisplay
import com.bogdan801.romanconverter.presentation.components.InputKeyboard
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