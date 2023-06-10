package com.lorenzovainigli.foodexpirationdates.model

import android.content.Context
import androidx.activity.ComponentActivity
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PreferencesProvider {

    companion object {

        private const val sharedPrefsName = "shared_pref"
        private const val keyDateFormat = "date_format"
        private const val keyNotificationTimeHour = "notification_time_hour"
        private const val keyNotificationTimeMinute = "notification_time_minute"
        private const val darkTheme = "dark_theme"
        private const val dynamicColors = "dynamic_colors"
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
        enum class OnOffSystem(val label: String) {
            ON("On"),
            SYSTEM("System"),
            OFF("Off")
        }

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

        fun getUserNotificationTimeHour(context: Context): Int {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .getInt(keyNotificationTimeHour, 11)
        }

        fun getUserNotificationTimeMinute(context: Context): Int {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .getInt(keyNotificationTimeMinute, 0)
        }

        fun setUserNotificationTime(context: Context, hour: Int, minute: Int) {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .edit().putInt(keyNotificationTimeHour, hour)
                .putInt(keyNotificationTimeMinute, minute).apply()
        }

        fun getDarkTheme(context: Context): Int {
            try {
                return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                    .getInt(darkTheme, OnOffSystem.SYSTEM.ordinal)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return OnOffSystem.SYSTEM.ordinal
        }

        fun setDarkTheme(context: Context, darkThemeSetting: OnOffSystem) {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .edit().putInt(darkTheme, darkThemeSetting.ordinal).apply()
        }

        fun getDynamicColors(context: Context): Boolean {
            try {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .getBoolean(dynamicColors, false)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return false
        }

        fun setDynamicColors(context: Context, dynamicColorsEnabled: Boolean) {
            return context.getSharedPreferences(sharedPrefsName, ComponentActivity.MODE_PRIVATE)
                .edit().putBoolean(dynamicColors, dynamicColorsEnabled).apply()
        }
    }
}