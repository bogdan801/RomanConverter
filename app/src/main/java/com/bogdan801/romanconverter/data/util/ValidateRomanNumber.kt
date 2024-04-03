package com.bogdan801.romanconverter.data.util

fun isRomanNumberValid(number: String, considerUppercase: Boolean = true): Boolean {
    val pattern =
        "^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?(I{0,3}|m{0,3}))(cm|cd|d?c{0,3})(xc|xl|l?x{0,3})(ix|iv|v?i{0,3})\$"
    val regex = Regex(pattern)
    return regex.matches(if(considerUppercase) number else number.lowercase())
}