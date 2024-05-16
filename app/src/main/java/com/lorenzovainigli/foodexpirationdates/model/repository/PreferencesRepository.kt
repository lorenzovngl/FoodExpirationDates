package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import androidx.activity.ComponentActivity
import com.lorenzovainigli.foodexpirationdates.R
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PreferencesRepository {

    companion object {

        private const val sharedPrefsName = "shared_pref"
        const val keyDateFormat = "date_format"
        const val keyNotificationTimeHour = "notification_time_hour"
        const val keyNotificationTimeMinute = "notification_time_minute"
        const val keyThemeMode = "theme_mode"
        const val keyTopBarFont = "top_bar_font"
        const val keyDynamicColors = "dynamic_colors"
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
        enum class ThemeMode(val label: Int) {
            LIGHT(R.string.light),
            SYSTEM(R.string.system),
            DARK(R.string.dark)
        }

        enum class TopBarFont(val label: Int) {
            NORMAL(R.string.normal),
            BOLD(R.string.bold),
            EXTRA_BOLD(R.string.extra_bold)
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

        fun getUserDateFormat(
            context: Context,
            sharedPrefs: String = sharedPrefsName
        ): String {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getString(keyDateFormat, "d MMM") ?: "d MMM"
        }

        fun setUserDateFormat(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            dateFormat: String
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putString(keyDateFormat, dateFormat).apply()
        }

        fun getUserNotificationTimeHour(
            context: Context,
            sharedPrefs: String = sharedPrefsName
        ): Int {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getInt(keyNotificationTimeHour, 11)
        }

        fun getUserNotificationTimeMinute(
            context: Context,
            sharedPrefs: String = sharedPrefsName
        ): Int {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getInt(keyNotificationTimeMinute, 0)
        }

        fun setUserNotificationTime(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            hour: Int,
            minute: Int
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putInt(keyNotificationTimeHour, hour)
                .putInt(keyNotificationTimeMinute, minute).apply()
        }

        fun getThemeMode(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
        ): Int {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getInt(keyThemeMode, ThemeMode.SYSTEM.ordinal)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return ThemeMode.SYSTEM.ordinal
        }

        fun setThemeMode(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            themeMode: ThemeMode
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putInt(keyThemeMode, themeMode.ordinal).apply()
        }

        fun getTopBarFont(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
        ): Int{
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getInt(keyTopBarFont,TopBarFont.NORMAL.ordinal)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return TopBarFont.NORMAL.ordinal
        }

        fun setTopBarFont(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            topBarFont: TopBarFont
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putInt(keyTopBarFont, topBarFont.ordinal).apply()
        }

        fun getDynamicColors(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
        ): Boolean {
            try {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getBoolean(keyDynamicColors, false)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return false
        }

        fun setDynamicColors(
            context: Context,
            sharedPrefs: String = sharedPrefsName,
            dynamicColorsEnabled: Boolean
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit().putBoolean(keyDynamicColors, dynamicColorsEnabled).apply()
        }
    }
}