package com.bogdan801.romanconverter.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

class TransitionsUtil {
    companion object {
        fun enterSlideInTransition(
            speed: Int = 200,
            isPopTransition: Boolean = false
        ): @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?){
            return if(!isPopTransition) {
                val enterTransition: @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
                    {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                            animationSpec = tween(speed)
                        )
                    }
                enterTransition
            }
            else {
                val popEnterTransition: @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
                    {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                            animationSpec = tween(speed)
                        )
                    }
                popEnterTransition
            }
        }
        fun exitSlideInTransition(
            speed: Int = 200,
            isPopTransition: Boolean = false
        ): @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?){
            return if(!isPopTransition) {
                val exitTransition: @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
                    {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                            animationSpec = tween(speed)
                        )
                    }
                exitTransition
            }
            else {
                val popExitTransition: @JvmSuppressWildcards (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
                    {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                            animationSpec = tween(speed)
                        )
                    }
                popExitTransition
            }
        }
    }
}