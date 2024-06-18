package com.bogdan801.romanconverter.presentation.screens.convert

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.R
import com.bogdan801.romanconverter.data.util.convertArabicToRoman
import com.bogdan801.romanconverter.data.util.convertRomanToArabic
import com.bogdan801.romanconverter.presentation.screens.convert.components.ConvertDisplay
import com.bogdan801.romanconverter.presentation.components.InputKeyboard
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import com.bogdan801.util_library.intSettings
import kotlinx.coroutines.launch

@Composable
fun ConvertScreen(
    navController: NavHostController,
    viewModel: ConvertViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current

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

    //Easter Egg activation
    val eggState by context.intSettings["egg"].collectAsStateWithLifecycle(initialValue = null)
    val scope = rememberCoroutineScope()
    val mediaPlayer = remember { MediaPlayer.create(context, R.raw.sfx_egg) }
    LaunchedEffect(key1 = screenState.arabicValue) {
        if(screenState.arabicValue == "787898"){
            mediaPlayer.start()
            if(eggState == null) {
                Toast.makeText(
                    context,
                    "Hehehe. You found the Easter Egg\uD83D\uDC23.\nEnjoy the app without ads",
                    Toast.LENGTH_LONG
                ).show()
                scope.launch {
                    context.intSettings.set("egg", 1)
                }
            }
        }
    }
}