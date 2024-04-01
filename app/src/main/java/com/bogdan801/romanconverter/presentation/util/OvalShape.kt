package com.bogdan801.romanconverter.presentation.util

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.toRect

val OvalShape = GenericShape { size, _ -> addOval(size.toRect()) }