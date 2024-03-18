package com.bogdan801.romanconverter.presentation.screens.home

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bogdan801.romanconverter.presentation.navigation.Screen
import com.bogdan801.romanconverter.presentation.navigation.TransitionsUtil

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        var previousRoute by remember { mutableStateOf("") }
        NavHost(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            navController = navController,
            startDestination = Screen.Convert.route
        ){
            composable(
                route = Screen.Convert.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200)
                    )
                },
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = navController.currentDestination?.route ?: "", color = Color.White, fontSize = 46.sp)
                }
            }

            composable(
                route = Screen.Quiz.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = when(previousRoute){
                            Screen.Convert.route -> AnimatedContentTransitionScope.SlideDirection.Companion.Left
                            Screen.Camera.route -> AnimatedContentTransitionScope.SlideDirection.Companion.Right
                            else -> AnimatedContentTransitionScope.SlideDirection.Companion.Left
                        },
                        animationSpec = tween(200)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = when(navController.currentDestination?.route ?: ""){
                            Screen.Convert.route -> AnimatedContentTransitionScope.SlideDirection.Companion.Right
                            Screen.Camera.route -> AnimatedContentTransitionScope.SlideDirection.Companion.Left
                            else -> AnimatedContentTransitionScope.SlideDirection.Companion.Left
                        },
                        animationSpec = tween(200)
                    )
                },
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = navController.currentDestination?.route ?: "", color = Color.White, fontSize = 46.sp)
                }
            }

            composable(
                route = Screen.Camera.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(200)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(200)
                    )
                },
            ){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = navController.currentDestination?.route ?: "", color = Color.White, fontSize = 46.sp)
                }
            }
        }

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
    }


    /*val currentTheme = context.intSettings["theme"].collectAsState(initial = 2)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = screenState.someValue,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                scope.launch {
                    context.intSettings.set("theme", ((currentTheme.value ?: 0) + 1) % 3)
                }
            }
        ) {
            Text(
                text = when(currentTheme.value){
                    0 -> "Світла"
                    1 -> "Темна"
                    else -> "Авто"
                }
            )
        }
    }*/
}