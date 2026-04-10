package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.view.Window
import android.view.WindowManager
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.Language
import java.lang.Exception
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.content.edit

class PreferencesRepository {

    companion object {

        private const val SHARED_PREFS_NAME = "shared_pref"
        const val KEY_DATE_FORMAT = "date_format"
        const val KEY_SCREEN_PROTECTION = "screen_protection"
        const val KEY_NOTIFICATION_TIME_HOUR = "notification_time_hour"
        const val KEY_NOTIFICATION_TIME_MINUTE = "notification_time_minute"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_TOP_BAR_FONT = "top_bar_font"
        const val KEY_DYNAMIC_COLORS = "dynamic_colors"
        const val KEY_MONOCHROME_ICONS = "monochrome_icons"
        const val KEY_LANGUAGE = "language"
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

        fun checkAndSetSecureFlags(context: Context, window: Window) {
            val isScreenProtectionEnabled = getScreenProtectionEnabled(context)

            if (isScreenProtectionEnabled) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }

        fun setScreenProtectionEnabled(context: Context, enabled: Boolean) {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            sharedPreferences.edit {
                putBoolean(KEY_SCREEN_PROTECTION, enabled)
            }
        }

        fun getScreenProtectionEnabled(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(KEY_SCREEN_PROTECTION, false)
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
            sharedPrefs: String = SHARED_PREFS_NAME
        ): String {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getString(KEY_DATE_FORMAT, "d MMM") ?: "d MMM"
        }

        fun setUserDateFormat(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            dateFormat: String
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit { putString(KEY_DATE_FORMAT, dateFormat) }
        }

        fun getUserNotificationTimeHour(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME
        ): Int {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getInt(KEY_NOTIFICATION_TIME_HOUR, 11)
        }

        fun getUserNotificationTimeMinute(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME
        ): Int {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .getInt(KEY_NOTIFICATION_TIME_MINUTE, 0)
        }

        fun setUserNotificationTime(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            hour: Int,
            minute: Int
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit {
                    putInt(KEY_NOTIFICATION_TIME_HOUR, hour)
                    putInt(KEY_NOTIFICATION_TIME_MINUTE, minute)
                }
        }

        fun getThemeMode(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
        ): Int {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getInt(KEY_THEME_MODE, ThemeMode.SYSTEM.ordinal)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return ThemeMode.SYSTEM.ordinal
        }

        fun setThemeMode(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            themeMode: ThemeMode
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit { putInt(KEY_THEME_MODE, themeMode.ordinal) }
        }

        fun getTopBarFont(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
        ): Int{
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getInt(KEY_TOP_BAR_FONT,TopBarFont.NORMAL.ordinal)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return TopBarFont.NORMAL.ordinal
        }

        fun setTopBarFont(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            topBarFont: TopBarFont
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit { putInt(KEY_TOP_BAR_FONT, topBarFont.ordinal) }
        }

        fun getDynamicColors(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
        ): Boolean {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getBoolean(KEY_DYNAMIC_COLORS, false)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return false
        }

        fun setDynamicColors(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            dynamicColorsEnabled: Boolean
        ): Boolean {
            try {
                context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .edit { putBoolean(KEY_DYNAMIC_COLORS, dynamicColorsEnabled) }
                return true
            } catch (_: Exception){
                return false
            }
        }

        fun getMonochromeIcons(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
        ): Boolean {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getBoolean(KEY_MONOCHROME_ICONS, true)
            } catch (e: Exception){
                e.printStackTrace()
            }
            return true
        }

        fun setMonochromeIcons(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            monochromeIconsEnabled: Boolean
        ): Boolean {
            try {
                context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .edit { putBoolean(KEY_MONOCHROME_ICONS, monochromeIconsEnabled) }
                return true
            } catch (_: Exception){
                return false
            }
        }

        fun getLanguage(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
        ): String {
            try {
                return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                    .getString(KEY_LANGUAGE, Language.SYSTEM.code)
                    ?: Language.SYSTEM.code
            } catch (e: Exception){
                e.printStackTrace()
            }
            return Language.SYSTEM.code
        }

        fun setLanguage(
            context: Context,
            sharedPrefs: String = SHARED_PREFS_NAME,
            language: String
        ) {
            return context.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE)
                .edit { putString(KEY_LANGUAGE, language) }
        }
    }
}