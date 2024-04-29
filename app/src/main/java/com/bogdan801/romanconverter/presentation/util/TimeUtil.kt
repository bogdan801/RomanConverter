package com.bogdan801.romanconverter.presentation.util

fun Int.secondsToTimeString(): String {
    val minutes = this / 60
    val seconds = this % 60

    val paddedMinutes = minutes.toString().padStart(2, '0').takeLast(2)
    val paddedSeconds = seconds.toString().padStart(2, '0').takeLast(2)

    return "$paddedMinutes:$paddedSeconds"
}