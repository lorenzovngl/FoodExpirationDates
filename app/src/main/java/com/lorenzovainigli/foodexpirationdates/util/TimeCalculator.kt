package com.lorenzovainigli.foodexpirationdates.util

import java.util.Calendar

object TimeCalculator {
    fun calculateDelayToNext(targetHour: Int, targetMinute: Int, currentTime: Calendar): Long {
        val dueTime = currentTime.clone() as Calendar
        dueTime.set(Calendar.HOUR_OF_DAY, targetHour)
        dueTime.set(Calendar.MINUTE, targetMinute)
        dueTime.set(Calendar.SECOND, 0)
        dueTime.set(Calendar.MILLISECOND, 0)

        if (currentTime.timeInMillis >= dueTime.timeInMillis) {
            dueTime.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dueTime.timeInMillis - currentTime.timeInMillis
    }
}