package com.ayo.ctmtest

import org.junit.Test

import org.junit.Assert.*


class PrimeNumberTest {

    @Test
    fun is_prime() {
        //GIVEN
        val number = 383

        //THEN
        assertTrue(number.isPrimeCompute())
    }

    @Test
    fun is_not_prime() {
        //GIVEN
        val number = 382

        //THEN
        assertFalse(number.isPrimeCompute())

    }
}
