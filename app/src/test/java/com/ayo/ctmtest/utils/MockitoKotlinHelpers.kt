package com.ayo.ctmtest.utils

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

object MockitoKotlinHelpers {
    // hack to fix Mockito.any() failure when used with Kotlin
    fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T

    /**
     * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
     * when null is returned.
     */
    fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}
