package com.ayo.ctmtest

fun Int.isPrime(): Boolean {
    var toReturn = false
    for (i in 2..this / 2) {
        if (this % i == 0) {
            toReturn = true
            break
        }
    }
    return toReturn
}