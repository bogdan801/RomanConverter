package com.bogdan801.romanconverter.presentation.screens.convert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.presentation.components.InputButton
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
            var value by remember { mutableStateOf("") }
            var type by remember { mutableStateOf(InputKeyboardType.Roman) }
            Text(
                text = value,
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(48.dp))
            InputKeyboard(
                value = value,
                type = type,
                onValueChange = {
                    value = it
                },
                onTypeChange = {
                    type = it
                }
            )
        }
    }
}