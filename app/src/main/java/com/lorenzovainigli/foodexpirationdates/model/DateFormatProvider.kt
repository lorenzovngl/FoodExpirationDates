package com.lorenzovainigli.foodexpirationdates.model

import android.content.Context
import androidx.activity.ComponentActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class DateFormatProvider {

    companion object {

        private const val sharedPrefsName = "shared_pref"
        private const val keyDateFormat = "date_format"
        private val availLocaleDateFormats = arrayOf(DateFormat.SHORT, DateFormat.MEDIUM)
        private val availOtherDateFormats =
            arrayOf(
                "d MMM",
                "d MMM yyyy",
                "d MMMM yyyy",
                "yyyy-MM-dd",
                "MM-dd",
                "d/MM",
                "d/MM/yyyy"
            )

        fun getAvailLocaleDateFormats(): List<String> {
            return availLocaleDateFormats.map {
                (DateFormat.getDateInstance(
                    it, Locale.getDefault()
                ) as SimpleDateFormat).toLocalizedPattern()
            }
        }

        fun getAvailOtherDateFormats(): Array<String> {
            return availOtherDateFormats
        }

        fun getUserDateFormat(context: Context): String {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .getString(keyDateFormat, "d MMM") ?: "d MMM"
        }

        fun setUserDateFormat(context: Context, dateFormat: String) {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .edit().putString(keyDateFormat, dateFormat).apply()
        }

    }
}