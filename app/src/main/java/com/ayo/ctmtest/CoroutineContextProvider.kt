package com.ayo.ctmtest

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.*

open class CoroutineContextProvider {
    open val Main: CoroutineContext by lazy { Dispatchers.Main }
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
}