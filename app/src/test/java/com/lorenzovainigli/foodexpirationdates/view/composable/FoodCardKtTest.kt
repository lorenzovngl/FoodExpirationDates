package com.lorenzovainigli.foodexpirationdates.view.composable

import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class FoodCardKtTest {

    private fun String.stringToCalendar(): Calendar {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = dateFormat.parse(this)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        return calendar
    }

    private fun Calendar.calendarToString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return dateFormat.format(this.time)
    }

    private fun Long.timestampToString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = Date(this)
        return dateFormat.format(date)
    }

    private val dec15 = "15/12/2023"

    @Test
    fun computeExpirationDateTest1() {
        assertEquals(
            dec15,
            computeExpirationDate(
                expirationDate = dec15.stringToCalendar().time.time
            ).timestampToString()
        )
    }

    @Test
    fun computeExpirationDateTest2() {
        assertEquals(
            "12/12/2023",
            computeExpirationDate(
                expirationDate = dec15.stringToCalendar().time.time,
                openingDate = "10/12/2023".stringToCalendar().time.time,
                timeSpanDays = 2
            ).timestampToString()
        )
    }

    @Test
    fun computeExpirationDateTest3() {
        assertEquals(
            dec15,
            computeExpirationDate(
                expirationDate = dec15.stringToCalendar().time.time,
                openingDate = "14/12/2023".stringToCalendar().time.time,
                timeSpanDays = 4
            ).timestampToString()
        )
    }

    @Test
    fun computeExpirationDateTest4() {
        val dec29 = "29/12/2023"
        val timeSpan = 15
        assertEquals(
            dec29.stringToCalendar().apply {
                add(Calendar.DAY_OF_MONTH, timeSpan)
            }.calendarToString(),
            computeExpirationDate(
                expirationDate = "31/01/2024".stringToCalendar().time.time,
                openingDate = dec29.stringToCalendar().time.time,
                timeSpanDays = timeSpan
            ).timestampToString()
        )
    }

}