package com.bogdan801.romanconverter.data.util

fun convertSecondsToTimeString(timeInSeconds: Int): String{
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds % 60

    val paddedMinutes = minutes.toString().padStart(2, '0').takeLast(2)
    val paddedSeconds = seconds.toString().padStart(2, '0').takeLast(2)

    return "$paddedMinutes:$paddedSeconds"
}