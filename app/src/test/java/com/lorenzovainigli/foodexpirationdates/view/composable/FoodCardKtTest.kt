package com.lorenzovainigli.foodexpirationdates.view.composable

import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat
import java.util.Calendar

class FoodCardKtTest {

    private fun stringToCalendar(dateString: String): Calendar {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = dateFormat.parse(dateString)
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = date
        }
        return calendar
    }

    @Test
    fun computeExpirationDateTest() {
        val expirationDate = stringToCalendar("15/12/2023").time.time
        assertEquals(
            expirationDate,
            computeExpirationDate(
                expirationDate = expirationDate
            ))
        assertEquals(
            stringToCalendar("12/12/2023").time.time,
            computeExpirationDate(
                expirationDate = expirationDate,
                openingDate = stringToCalendar("10/12/2023").time.time,
                timeSpanDays = 2
            )
        )
        assertEquals(
            expirationDate,
            computeExpirationDate(
                expirationDate = expirationDate,
                openingDate = stringToCalendar("14/12/2023").time.time,
                timeSpanDays = 4
            )
        )
    }

}