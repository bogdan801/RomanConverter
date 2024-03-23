package com.bogdan801.romanconverter.presentation.components

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bogdan801.romanconverter.data.util.isRomanNumberValid


private enum class KeyAction{
    Input, Backspace, ChangeType, ChangeCase
}

private data class KeyProperty(
    val label: String?,
    val offset: IntOffset = IntOffset.Zero,
    val isVisible: Boolean = true,
    val action: KeyAction = KeyAction.Input
)

private data class InputKey(
    val roman: KeyProperty,
    val arabic: KeyProperty
)
enum class InputKeyboardType{
    Roman, Arabic
}

@Composable
fun InputKeyboard(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (newValue: String) -> Unit,
    type: InputKeyboardType = InputKeyboardType.Roman,
    onTypeChange: (newType: InputKeyboardType) -> Unit
) {
    val density = LocalDensity.current
    var isRoman by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = type) {
        onValueChange("")
        isRoman = type == InputKeyboardType.Roman
    }

    val keys = remember {
        listOf(
            InputKey(
                roman = KeyProperty(
                    label = "i",
                    offset = IntOffset(
                        x = with(density) {0.dp.toPx().toInt()},
                        y = with(density) {8.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "7",
                    offset = IntOffset(
                        x = with(density) {40.dp.toPx().toInt()},
                        y = 0,
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "x",
                    offset = IntOffset(
                        x = with(density) {80.dp.toPx().toInt()},
                        y = with(density) {8.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "8",
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = 0,
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "m",
                    offset = IntOffset(
                        x = with(density) {240.dp.toPx().toInt()},
                        y = with(density) {8.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = null,
                    isVisible = false,
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = 0,
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "c",
                    offset = IntOffset(
                        x = with(density) {160.dp.toPx().toInt()},
                        y = with(density) {8.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "9",
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = 0,
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "v",
                    offset = IntOffset(
                        x = with(density) {40.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "4",
                    offset = IntOffset(
                        x = with(density) {40.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "2",
                    isVisible = false,
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "2",
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {160.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "l",
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "5",
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "d",
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "6",
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = with(density) {80.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "I",
                    offset = IntOffset(
                        x = with(density) {80.dp.toPx().toInt()},
                        y = with(density) {152.dp.toPx().toInt()},
                    ),
                    action = KeyAction.ChangeCase
                ),
                arabic = KeyProperty(
                    label = "1",
                    offset = IntOffset(
                        x = with(density) {40.dp.toPx().toInt()},
                        y = with(density) {160.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "9",
                    action = KeyAction.ChangeType,
                    offset = IntOffset(
                        x = with(density) {160.dp.toPx().toInt()},
                        y = with(density) {152.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "3",
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = with(density) {160.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = "x",
                    isVisible = false,
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {224.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "x",
                    action = KeyAction.ChangeType,
                    offset = IntOffset(
                        x = with(density) {40.dp.toPx().toInt()},
                        y = with(density) {240.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = null,
                    action = KeyAction.Backspace,
                    isVisible = false,
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {240.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = null,
                    action = KeyAction.Backspace,
                    offset = IntOffset(
                        x = with(density) {200.dp.toPx().toInt()},
                        y = with(density) {240.dp.toPx().toInt()},
                    )
                )
            ),
            InputKey(
                roman = KeyProperty(
                    label = null,
                    action = KeyAction.Backspace,
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {224.dp.toPx().toInt()},
                    )
                ),
                arabic = KeyProperty(
                    label = "0",
                    offset = IntOffset(
                        x = with(density) {120.dp.toPx().toInt()},
                        y = with(density) {240.dp.toPx().toInt()},
                    )
                )
            ),
        )
    }

    var isLowercase by remember { mutableStateOf(true) }
    Box(
        modifier = modifier
            .size(304.dp, 280.dp)
    ){
        keys.forEach { key ->
            val label = if(isRoman) {
                when{
                    key.roman.label == null -> null
                    !isLowercase -> {
                        if(key.roman.action == KeyAction.ChangeCase) "i"
                        else key.roman.label.uppercase()
                    }
                    else -> key.roman.label
                }
            }
            else key.arabic.label

            val isVisible = if(isRoman) key.roman.isVisible else key.arabic.isVisible

            val alpha by animateFloatAsState(
                targetValue = if(isVisible) 1f else 0f,
                label = ""
            )

            val keyOffset by animateIntOffsetAsState(
                targetValue = if(isRoman) key.roman.offset else key.arabic.offset,
                label = ""
            )
            val context = LocalContext.current
            var isEnabled by remember { mutableStateOf(true) }
            LaunchedEffect(key1 = type, key2 = value, key3 = isLowercase) {
                isEnabled = if(isRoman && (key.roman.action == KeyAction.Input)) {
                    isRomanNumberValid(value + label)
                }
                else true


                /*when {
                    isRoman -> {
                        if(key.roman.action == KeyAction.Input)
                            isRomanNumberValid(value + label)
                        else true
                    }
                    !isRoman -> {
                        !(key.arabic.label == "0" && value.isBlank())
                    }
                    else -> true
                }*/



            }

            InputButton(
                modifier = Modifier
                    .offset { keyOffset }
                    .alpha(alpha),
                label = label,
                isEnabled = alpha == 1f && isEnabled,
                isBackspace = if(isRoman) key.roman.action == KeyAction.Backspace
                              else        key.arabic.action == KeyAction.Backspace,
                onClick = {
                    if (isRoman){
                        when(key.roman.action) {
                            KeyAction.Input -> onValueChange(value + label)
                            KeyAction.Backspace -> {
                                if (value.isNotBlank()) onValueChange(value.dropLast(1))
                            }
                            KeyAction.ChangeType -> onTypeChange(InputKeyboardType.Arabic)
                            KeyAction.ChangeCase -> isLowercase = !isLowercase
                        }
                    }
                    else {
                        when(key.arabic.action){
                            KeyAction.Input -> onValueChange(value + label)
                            KeyAction.Backspace -> {
                                if (value.isNotBlank()) onValueChange(value.dropLast(1))
                            }
                            KeyAction.ChangeType -> onTypeChange(InputKeyboardType.Roman)
                            KeyAction.ChangeCase -> {}
                        }
                    }
                },
                onLongClick = {
                    if(
                        (isRoman && key.roman.action == KeyAction.Backspace) ||
                        (!isRoman && key.arabic.action == KeyAction.Backspace)
                    ){
                        onValueChange("")
                    }
                }
            )
        }
    }
    
    
    
   /* Column(
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
    }*/
}