package com.bogdan801.romanconverter.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.bogdan801.romanconverter.R

val libreRomanFontFamily = FontFamily(Font(R.font.libreroman_regular, FontWeight.Normal))
val libreBodoniFontFamily = FontFamily(
    Font(R.font.librebodoni_regular, FontWeight.Normal),
    Font(R.font.librebodoni_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = libreRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp
    ),
    displayMedium = TextStyle(
        fontFamily = libreRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    displaySmall = TextStyle(
        fontFamily = libreRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = libreRomanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = libreBodoniFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)