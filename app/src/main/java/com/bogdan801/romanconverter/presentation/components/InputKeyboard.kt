package com.bogdan801.romanconverter.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InputKeyboard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputButton(label = "i")
            InputButton(label = "x")
            InputButton(label = "c")
            InputButton(label = "m")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputButton(label = "v")
            InputButton(label = "l")
            InputButton(label = "d")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputButton(label = "9")
            InputButton(label = "I")
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputButton(isBackspace = true)
        }
    }
}