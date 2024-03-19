package com.bogdan801.romanconverter.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    previousRoute: String = "",
    convertScreen: @Composable () -> Unit,
    quizScreen: @Composable () -> Unit,
    cameraScreen: @Composable () -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Convert.route
    ){
        composable(
            route = Screen.Convert.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
        ){
            convertScreen()
        }

        composable(
            route = Screen.Quiz.route,
            enterTransition = {
                slideIntoContainer(
                    towards = when(previousRoute){
                        Screen.Convert.route -> AnimatedContentTransitionScope.SlideDirection.Left
                        Screen.Camera.route -> AnimatedContentTransitionScope.SlideDirection.Right
                        else -> AnimatedContentTransitionScope.SlideDirection.Left
                    },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = when(navController.currentDestination?.route ?: ""){
                        Screen.Convert.route -> AnimatedContentTransitionScope.SlideDirection.Right
                        Screen.Camera.route -> AnimatedContentTransitionScope.SlideDirection.Left
                        else -> AnimatedContentTransitionScope.SlideDirection.Left
                    },
                    animationSpec = tween(200)
                )
            },
        ){
            quizScreen()
        }

        composable(
            route = Screen.Camera.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(200)
                )
            },
        ){
            cameraScreen()
        }
    }
}