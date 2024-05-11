package com.bogdan801.romanconverter.presentation.util

fun mapRange(number: Int, prevRangeFirst: Int, prevRangeLast: Int, newRangeFirst: Int, newRangeLast: Int) : Int {
    val ratio = number.toFloat() / (prevRangeLast - prevRangeFirst)
    return (ratio * (newRangeLast - newRangeFirst) + newRangeFirst).toInt()
}