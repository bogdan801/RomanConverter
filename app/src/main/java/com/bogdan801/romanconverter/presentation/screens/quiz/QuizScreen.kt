package com.bogdan801.romanconverter.presentation.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.bogdan801.romanconverter.presentation.components.PauseDialogBox
import com.bogdan801.romanconverter.presentation.components.QuizOverDialogBox
import com.bogdan801.romanconverter.presentation.components.QuizTypeSelector
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
        QuizTypeSelector(
            modifier = Modifier.size(150.dp, 136.dp),
            isSelected = true
        )
        /*var value by remember { mutableIntStateOf(0) }
        var started by remember { mutableStateOf(false) }
        var show by remember { mutableStateOf(false) }
        var showLoseDialog by remember { mutableStateOf(false) }
        LaunchedEffect(key1 = started) {
            if(started){
                while(value > 0){
                    delay(1000)
                    value--
                }
                if(value == 0) {
                    started = false
                    showLoseDialog = true
                }
            }
        }

        ActionButton(
            label = "OPEN",
            onClick = {
                show = !show
            }
        )
        Spacer(modifier = Modifier.height(48.dp))

        Row {
            ActionButton(
                label = "+5",
                onClick = {
                    value += 5
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            ActionButton(
                label = "-5",
                onClick = {
                    if(value-5 >= 0) value -= 5
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
        ActionButton(
            label = if(!started) "START" else "STOP",
            onClick = {
                started = !started
            }
        )
        Spacer(modifier = Modifier.height(48.dp))
        TimeCounter(
            modifier = Modifier.size(100.dp, 30.dp),
            value = value
        )
        Spacer(modifier = Modifier.height(48.dp))
        ActionButton(
            label = if(homeScreenState.isNavBarExpanded) "HIDE" else "SHOW",
            onClick = {
                homeViewModel.showNavBar(!homeScreenState.isNavBarExpanded)
            }
        )

        PauseDialogBox(
            modifier = Modifier,
            show = show,
            onVisibilityChanged = { isVisible ->
                homeViewModel.blurBackground(isVisible)
            },
            onDismiss = {
                show = false
            },
            onHomeClick = {}
        )
        QuizOverDialogBox(
            modifier = Modifier,
            show = showLoseDialog,
            onVisibilityChanged = { isVisible ->
                homeViewModel.blurBackground(isVisible)
            },
            onDismiss = {
                showLoseDialog = false
            },
            score = 1639,
            count = 23,
            onHomeClick = {}
        )*/
    }
}