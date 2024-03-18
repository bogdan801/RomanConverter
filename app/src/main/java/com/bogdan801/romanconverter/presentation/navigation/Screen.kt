package com.bogdan801.romanconverter.presentation.navigation

sealed class Screen(val route: String) {
    object Convert : Screen("convert")
    object Quiz : Screen("quiz")
    object Camera : Screen("camera")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}