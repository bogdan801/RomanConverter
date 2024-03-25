package com.bogdan801.romanconverter.data.util

fun convertRomanToArabic(romanNumber: String): String {
    val lookup = mapOf(
        'M' to 1000000,
        'D' to 500000,
        'C' to 100000,
        'L' to 50000,
        'X' to 10000,
        'V' to 5000,
        'I' to 1000,
        'm' to 1000,
        'd' to 500,
        'c' to 100,
        'l' to 50,
        'x' to 10,
        'v' to 5,
        'i' to 1
    )

    var output = 0
    romanNumber.forEachIndexed { i, char ->
        val nextChar = if(i+1 <= romanNumber.lastIndex) romanNumber[i+1] else null
        if(nextChar == null) output += lookup[char]!!
        else {
            if(lookup[nextChar]!! > lookup[char]!!) output -= lookup[char]!!
            else output += lookup[char]!!
        }
    }
    return if(output != 0) output.toString() else ""
}

fun convertArabicToRoman(arabicNumber: String): String {
    var output = ""
    arabicNumber.forEachIndexed { i, c ->
        val index = arabicNumber.lastIndex - i
        output += when(index){
            6 -> {
                when(c){
                    '1' -> "M"
                    '2' -> "MM"
                    '3' -> "MMM"
                    else -> ""
                }
            }
            5 -> {
                when(c){
                    '1' -> "C"
                    '2' -> "CC"
                    '3' -> "CCC"
                    '4' -> "CD"
                    '5' -> "D"
                    '6' -> "DC"
                    '7' -> "DCC"
                    '8' -> "DCCC"
                    '9' -> "CM"
                    else -> ""
                }
            }
            4 -> {
                when(c){
                    '1' -> "X"
                    '2' -> "XX"
                    '3' -> "XXX"
                    '4' -> "XL"
                    '5' -> "L"
                    '6' -> "LX"
                    '7' -> "LXX"
                    '8' -> "LXXX"
                    '9' -> "XC"
                    else -> ""
                }
            }
            3 -> {
                when(c){
                    '1' -> if(arabicNumber.length > 4) "I" else "m"
                    '2' -> if(arabicNumber.length > 4) "II" else "mm"
                    '3' -> if(arabicNumber.length > 4) "III" else "mmm"
                    '4' -> "IV"
                    '5' -> "V"
                    '6' -> "VI"
                    '7' -> "VII"
                    '8' -> "VIII"
                    '9' -> "IX"
                    else -> ""
                }
            }
            2 -> {
                when(c){
                    '1' -> "c"
                    '2' -> "cc"
                    '3' -> "ccc"
                    '4' -> "cd"
                    '5' -> "d"
                    '6' -> "dc"
                    '7' -> "dcc"
                    '8' -> "dccc"
                    '9' -> "cm"
                    else -> ""
                }
            }
            1 -> {
                when(c){
                    '1' -> "x"
                    '2' -> "xx"
                    '3' -> "xxx"
                    '4' -> "xl"
                    '5' -> "l"
                    '6' -> "lx"
                    '7' -> "lxx"
                    '8' -> "lxxx"
                    '9' -> "xc"
                    else -> ""
                }
            }
            0 -> {
                when(c){
                    '1' -> "i"
                    '2' -> "ii"
                    '3' -> "iii"
                    '4' -> "iv"
                    '5' -> "v"
                    '6' -> "vi"
                    '7' -> "vii"
                    '8' -> "viii"
                    '9' -> "ix"
                    else -> ""
                }
            }
            else -> ""
        }
    }
    return output
}