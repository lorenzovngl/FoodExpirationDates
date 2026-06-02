package com.lorenzovainigli.foodexpirationdates.util

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.util.Calendar

class TimeCalculatorTest {

    @Test
    fun `if notification is before current time, calculate delay for tomorrow`() {
        // Mock the current time to 10:00
        val currentTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 10)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Target notification time is 08:00 (already passed)
        val delay = TimeCalculator.calculateDelayToNext(8, 0, currentTime)

        // Expected delay is 22 hours (in milliseconds)
        val expectedDelay = 22L * 60L * 60L * 1000L
        assertEquals(expectedDelay, delay)
    }

    @Test
    fun `if notification is after current time, calculate delay for today`() {
        // Mock the current time to 06:00
        val currentTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 6)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        // Target notification time is 08:00 (yet to come)
        val delay = TimeCalculator.calculateDelayToNext(8, 0, currentTime)

        // Expected delay is 2 hours (in milliseconds)
        val expectedDelay = 2L * 60L * 60L * 1000L
        assertEquals(expectedDelay, delay)
    }

}