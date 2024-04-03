package com.bogdan801.romanconverter.presentation.components

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.data.util.isRomanNumberValid
import com.bogdan801.romanconverter.presentation.theme.displayGradientBrush
import com.bogdan801.romanconverter.presentation.theme.iconButtonGradientBrush
import com.bogdan801.romanconverter.presentation.util.shadowCustom

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecognizedTextDisplay(
    modifier: Modifier = Modifier,
    recognizedText: String = ""
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var listOfWords by remember { mutableStateOf(mapOf<String, Boolean>()) }
    LaunchedEffect(key1 = recognizedText) {
        if(recognizedText.isNotBlank()){
            val map: MutableMap<String, Boolean> = mutableMapOf()
            recognizedText.split(" ").forEach {
                if(isRomanNumberValid(it, considerUppercase = false)){
                    map[convertRomanToArabic(it.lowercase())] = true
                }
                else map[it] = false
            }
            listOfWords = map
        }
    }
    val convertedString by remember {
        derivedStateOf {
            buildString {
                listOfWords.entries.forEach {
                    append(it.key + " ")
                }
            }
        }
    }


    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(2f)
            .shadowCustom(
                color = Color.Black.copy(alpha = 0.2f),
                shapeRadius = 12.dp,
                blurRadius = 30.dp,
                offsetY = 10.dp
            )
            .clip(RoundedCornerShape(12.dp))
            .background(displayGradientBrush())
            .combinedClickable(
                interactionSource = null,
                indication = null,
                onClick = {},
                onLongClick = {
                    if (recognizedText.isNotBlank()) {
                        clipboardManager.setText(AnnotatedString(convertedString))
                        Toast
                            .makeText(context, "Copied to a clipboard", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            ),
        contentAlignment = Alignment.Center
    ){
        val width = maxWidth
        val height = maxHeight
        Icon(
            modifier = Modifier
                .padding(top = 4.dp)
                .width(width - 16.dp)
                .height((width - 16.dp) / 10)
                .align(Alignment.TopCenter),
            painter = painterResource(id = R.drawable.ornament_text_frame),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onTertiary
        )
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(
                    top = height * 0.1f,
                    bottom = 0.dp,
                    start = 8.dp,
                    end = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                painter = painterResource(id = R.drawable.ornament_main_display_left),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
            Icon(
                painter = painterResource(id = R.drawable.ornament_main_display_right),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Icon(
                modifier = Modifier.rotate(180f),
                painter = painterResource(id = R.drawable.ornament_main_display_right),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
            Icon(
                modifier = Modifier.rotate(180f),
                painter = painterResource(id = R.drawable.ornament_main_display_left),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onTertiary
            )
        }


        if (recognizedText.isNotBlank()){
            val primary = MaterialTheme.colorScheme.primary
            val secondary = MaterialTheme.colorScheme.secondary
            val annotatedString = buildAnnotatedString {
                listOfWords.entries.forEach {
                    if(!it.value) append(it.key)
                    else {
                        withStyle(
                            style = SpanStyle(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        primary, secondary, primary
                                    )
                                ),
                                fontWeight = FontWeight.Bold)
                        ) {
                            append(it.key)
                        }
                    }
                    append(" ")
                }
            }

            AutoSizeText(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .offset(y = 8.dp),
                text = annotatedString,
                maxLines = 3,
                minTextSize = 16.sp,
                maxTextSize = 20.sp,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiary,
                textAlign = TextAlign.Center
            )
        }
        else {
            Text(
                modifier = Modifier.offset(y = 8.dp),
                text = "TEXT IS NOT DETECTED",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}