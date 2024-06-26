package com.bogdan801.romanconverter.presentation.screens.convert

import com.bogdan801.romanconverter.presentation.components.InputKeyboardType

data class ConvertScreenState(
    val romanValue: String = "",
    val arabicValue: String = "",
    val type: InputKeyboardType = InputKeyboardType.Roman
)
