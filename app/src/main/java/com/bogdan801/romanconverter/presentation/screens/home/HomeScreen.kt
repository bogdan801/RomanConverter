package com.bogdan801.romanconverter.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bogdan801.romanconverter.presentation.navigation.Navigation
import com.bogdan801.romanconverter.presentation.navigation.Screen
import com.bogdan801.romanconverter.presentation.screens.camera.CameraScreen
import com.bogdan801.romanconverter.presentation.screens.convert.ConvertScreen
import com.bogdan801.romanconverter.presentation.screens.quiz.QuizScreen
import com.bogdan801.util_library.intSettings
import com.bogdan801.util_library.stringSettings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /*Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        var previousRoute by remember { mutableStateOf("") }

        Navigation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            navController = navController,
            previousRoute = previousRoute,
            convertScreen = {
                ConvertScreen(
                    navController = navController,
                    homeViewModel = viewModel
                )
            },
            quizScreen = {
                QuizScreen(
                    navController = navController,
                    homeViewModel = viewModel
                )
            },
            cameraScreen = {
                CameraScreen(
                    navController = navController,
                    homeViewModel = viewModel
                )
            }
        )

        Row(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    previousRoute = navController.currentDestination?.route ?: ""
                    if(navController.currentDestination?.route!=Screen.Convert.route){
                        navController.navigate(Screen.Convert.route){
                            popUpTo(0)
                        }
                    }
                }
            ) {
                Text(text = "1")
            }
            Button(
                onClick = {
                    previousRoute = navController.currentDestination?.route ?: ""
                    if(navController.currentDestination?.route!=Screen.Quiz.route){
                        navController.navigate(Screen.Quiz.route){
                            popUpTo(0)
                        }
                    }
                }
            ) {
                Text(text = "2")
            }
            Button(
                onClick = {
                    previousRoute = navController.currentDestination?.route ?: ""
                    if(navController.currentDestination?.route!=Screen.Camera.route){
                        navController.navigate(Screen.Camera.route){
                            popUpTo(0)
                        }
                    }
                }
            ) {
                Text(text = "3")
            }
        }
    }*/


    val currentTheme = context.intSettings["theme"].collectAsState(initial = 2)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = screenState.someValue,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        val buttonEnabled = remember { mutableStateOf(true) }
        Button(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                scope.launch {
                    if(context.stringSettings["coords"].first() == null) {
                        val coors = coordinates.boundsInRoot().center
                        context.stringSettings.set("coords", "${coors.x};${coors.y}")
                    }
                }
            },
            onClick = {
                if (buttonEnabled.value) {
                    // Perform button action
                    buttonEnabled.value = false
                    scope.launch {
                        context.intSettings.set("theme", ((currentTheme.value ?: 0) + 1) % 2)
                    }
                    scope.launch {
                        delay(400)
                        buttonEnabled.value = true
                    }
                }
            },
            //enabled = buttonEnabled.value
        ) {
            Text(
                text = when(currentTheme.value){
                    0 -> "Світла"
                    1 -> "Темна"
                    else -> "Авто"
                }
            )
        }
    }
}