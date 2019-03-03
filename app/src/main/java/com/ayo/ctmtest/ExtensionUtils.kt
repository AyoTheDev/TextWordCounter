package com.ayo.ctmtest

fun Int.isPrime(): Boolean {
    var result = true
    for (i in 2 until this) {
        if (this % i == 0) {
            result = false
            break
        }
    }
    return result
}