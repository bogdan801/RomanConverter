package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bogdan801.romanconverter.presentation.components.ActionButton
import com.bogdan801.romanconverter.presentation.components.BaseDialogBox
import com.bogdan801.romanconverter.presentation.components.TimeCounter
import com.bogdan801.romanconverter.presentation.screens.home.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun QuizScreen(
    navController: NavHostController,
    viewModel: QuizViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val homeScreenState by homeViewModel.screenState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){


        var value by remember { mutableIntStateOf(20) }
        var started by remember { mutableStateOf(false) }
        var show by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = started) {
            if(started){
                while(value > 0){
                    delay(1000)
                    value--
                }
                if(value == 0) started = false
            }
        }
        Row {
            ActionButton(
                label = "+10",
                onClick = {
                    value += 10
                    show = !show
                    homeViewModel.blurBackground(true)
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton(
                label = "-10",
                onClick = {
                    value -= 10
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        ActionButton(
            label = if(!started) "Start" else "Stop",
            onClick = {
                started = !started
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
        TimeCounter(
            modifier = Modifier.size(100.dp, 30.dp),
            value = value
        )
        BaseDialogBox(
            modifier = Modifier.size(400.dp, 300.dp),
            show = show,
            onDismiss = {
                show = false
                homeViewModel.blurBackground(false)
            }
        ){
            Text(text = "текст", style = MaterialTheme.typography.displaySmall, color = MaterialTheme.colorScheme.onTertiary)
        }




        /*ActionButton(
            label = "Move",
            onClick = {
                if(homeScreenState.isExpanded) homeViewModel.hideNavBar()
                else homeViewModel.showNavBar()
            }
        )*/
    }
}