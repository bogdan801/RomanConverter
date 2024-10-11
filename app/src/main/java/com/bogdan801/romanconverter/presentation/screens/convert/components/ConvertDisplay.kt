package com.bogdan801.romanconverter.presentation.screens.convert.components

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.presentation.components.AutoSizeText
import com.bogdan801.romanconverter.presentation.components.InputKeyboardType
import com.bogdan801.romanconverter.presentation.theme.libreRomanFontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConvertDisplay(
    modifier: Modifier = Modifier,
    romanValue: String = "",
    arabicValue: String = "",
    type: InputKeyboardType = InputKeyboardType.Roman
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = modifier,
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

        val arabicFirstColor = if(type == InputKeyboardType.Arabic)
            MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onTertiary

        val arabicSecondColor = if(type == InputKeyboardType.Arabic)
            MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.onTertiary

        val arabicFontSize = if(type == InputKeyboardType.Arabic)
            MaterialTheme.typography.displaySmall.fontSize.value
        else MaterialTheme.typography.displayLarge.fontSize.value

        val baseOffset by animateDpAsState(
            targetValue = if(type == InputKeyboardType.Arabic) height * 0.30f
            else height * 0.55f,
            label = ""
        )

        AutoSizeText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset {
                    IntOffset(
                        x = 0,
                        y = with(this) {
                            ((height * 0.3f) + ((height * 0.55f) - baseOffset))
                                .toPx()
                                .toInt()
                        },
                    )
                }
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        if (arabicValue.isNotBlank()) {
                            clipboardManager.setText(AnnotatedString(text = arabicValue))
                            Toast
                                .makeText(
                                    context,
                                    arabicValue + " " + context.getString(R.string.clipboard).lowercase(),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                ),
            text = arabicValue.ifBlank { "0" },
            style = TextStyle(
                fontFamily = libreRomanFontFamily,
                fontSize = arabicFontSize.sp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        arabicFirstColor, arabicSecondColor, arabicFirstColor
                    )
                )
            ),
            textAlign = TextAlign.Center,
            maxTextSize = arabicFontSize.sp,
            minTextSize = 1.sp,
            softWrap = false
        )


        val romanFirstColor = if(type == InputKeyboardType.Roman)
            MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onTertiary

        val romanSecondColor = if(type == InputKeyboardType.Roman)
            MaterialTheme.colorScheme.secondary
        else MaterialTheme.colorScheme.onTertiary

        val romanFontSize = if(type == InputKeyboardType.Roman)
            MaterialTheme.typography.displaySmall.fontSize.value
        else MaterialTheme.typography.displayLarge.fontSize.value


        AutoSizeText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .offset {
                    IntOffset(
                        x = 0,
                        y = with(this) {
                            baseOffset
                                .toPx()
                                .toInt()
                        },
                    )
                }
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        if (romanValue.isNotBlank()) {
                            val output = if(romanValue.all { it.isLowerCase() })
                                romanValue.uppercase()
                            else romanValue

                            clipboardManager.setText(AnnotatedString(text = output))
                            Toast
                                .makeText(
                                    context,
                                    output + " " + context.getString(R.string.clipboard).lowercase(),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                ),
            text = romanValue.ifBlank { "O" },
            textAlign = TextAlign.Center,
            maxTextSize = romanFontSize.sp,
            minTextSize = 1.sp,
            style = TextStyle(
                fontFamily = libreRomanFontFamily,
                fontSize = arabicFontSize.sp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        romanFirstColor, romanSecondColor, romanFirstColor
                    )
                )
            ),
            softWrap = false
        )

        Icon(
            modifier = Modifier
                .size(width, height = width * (25f / 328f) * 1.5f)
                .align(Alignment.BottomCenter),
            painter = painterResource(id = R.drawable.ornament_main_display_bottom),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
    }
}