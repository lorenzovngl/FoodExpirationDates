package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.content.SharedPreferences
import android.view.Window
import android.view.WindowManager
import androidx.core.content.edit
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model.SettingsUiState
import com.lorenzovainigli.foodexpirationdates.model.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    companion object {
        const val SHARED_PREFS_NAME = "shared_pref"

        const val KEY_DATE_FORMAT = "date_format"
        const val KEY_SCREEN_PROTECTION = "screen_protection"
        const val KEY_NOTIFICATION_TIME_HOUR = "notification_time_hour"
        const val KEY_NOTIFICATION_TIME_MINUTE = "notification_time_minute"
        const val KEY_THEME_MODE = "theme_mode"
        const val KEY_TOP_BAR_FONT = "top_bar_font"
        const val KEY_DYNAMIC_COLORS = "dynamic_colors"
        const val KEY_MONOCHROME_ICONS = "monochrome_icons"
        const val KEY_LANGUAGE = "language"
        const val KEY_FOOD_ADDED_COUNT = "food_added_count"

        private val availLocaleDateFormats = arrayOf(
            DateFormat.SHORT,
            DateFormat.MEDIUM
        )

        private val availOtherDateFormats = arrayOf(
            "d MMM",
            "d MMM yyyy",
            "d MMMM yyyy",
            "yyyy-MM-dd",
            "MM-dd",
            "d/MM",
            "d/MM/yyyy"
        )
    }

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

    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )

    val settingsFlow: Flow<SettingsUiState> =
        callbackFlow {

            fun emitCurrentSettings() {
                trySend(getSettings())
            }

            val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                    emitCurrentSettings()
                }

            emitCurrentSettings()

            preferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                preferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.distinctUntilChanged()

    private fun getSettings(): SettingsUiState {
        return SettingsUiState(
            dateFormat = getUserDateFormat(),
            availLocaleDateFormats = getAvailLocaleDateFormats(),
            availOtherDateFormats = getAvailOtherDateFormats(),
            notificationHour = getUserNotificationTimeHour(),
            notificationMinute = getUserNotificationTimeMinute(),
            topBarFont = getTopBarFont(),
            themeMode = getThemeMode(),
            dynamicColorsEnabled = getDynamicColors(),
            monochromeIconsEnabled = getMonochromeIcons(),
            language = Language.fromCode(getLanguage()),
            screenProtectionEnabled = getScreenProtectionEnabled()
        )
    }

    // Date format

    fun getAvailLocaleDateFormats(): ImmutableList<String> {
        return availLocaleDateFormats.map {
            (
                    DateFormat.getDateInstance(
                        it,
                        Locale.getDefault()
                    ) as SimpleDateFormat
                    ).toLocalizedPattern()
        }.toImmutableList()
    }

    fun getAvailOtherDateFormats(): ImmutableList<String> {
        return availOtherDateFormats.toImmutableList()
    }

    fun getUserDateFormat(): String {
        return preferences.getString(
            KEY_DATE_FORMAT,
            "d MMM"
        ) ?: "d MMM"
    }

    fun setUserDateFormat(dateFormat: String) {
        preferences.edit {
            putString(KEY_DATE_FORMAT, dateFormat)
        }
    }

    // Notification time

    fun getUserNotificationTimeHour(): Int {
        return preferences.getInt(
            KEY_NOTIFICATION_TIME_HOUR,
            11
        )
    }

    fun getUserNotificationTimeMinute(): Int {
        return preferences.getInt(
            KEY_NOTIFICATION_TIME_MINUTE,
            0
        )
    }

    fun setUserNotificationTime(
        hour: Int,
        minute: Int
    ) {
        preferences.edit {
            putInt(KEY_NOTIFICATION_TIME_HOUR, hour)
            putInt(KEY_NOTIFICATION_TIME_MINUTE, minute)
        }
    }

    // Theme

    fun getThemeMode(): ThemeMode {
        val ordinal = preferences.getInt(
            KEY_THEME_MODE,
            ThemeMode.SYSTEM.ordinal
        )

        return ThemeMode.entries.getOrElse(ordinal) {
            ThemeMode.SYSTEM
        }
    }

    fun setThemeMode(themeMode: ThemeMode) {
        preferences.edit {
            putInt(
                KEY_THEME_MODE,
                themeMode.ordinal
            )
        }
    }

    // Top bar font

    fun getTopBarFont(): TopBarFont {
        val ordinal = preferences.getInt(
            KEY_TOP_BAR_FONT,
            TopBarFont.NORMAL.ordinal
        )

        return TopBarFont.entries.getOrElse(ordinal) {
            TopBarFont.NORMAL
        }
    }

    fun setTopBarFont(topBarFont: TopBarFont) {
        preferences.edit {
            putInt(
                KEY_TOP_BAR_FONT,
                topBarFont.ordinal
            )
        }
    }

    // Dynamic colors

    fun getDynamicColors(): Boolean {
        return preferences.getBoolean(
            KEY_DYNAMIC_COLORS,
            false
        )
    }

    fun setDynamicColors(enabled: Boolean) {
        preferences.edit {
            putBoolean(
                KEY_DYNAMIC_COLORS,
                enabled
            )
        }
    }

    // Monochrome icons

    fun getMonochromeIcons(): Boolean {
        return preferences.getBoolean(
            KEY_MONOCHROME_ICONS,
            true
        )
    }

    fun setMonochromeIcons(enabled: Boolean) {
        preferences.edit {
            putBoolean(
                KEY_MONOCHROME_ICONS,
                enabled
            )
        }
    }

    // Language

    fun getLanguage(): String {
        return preferences.getString(
            KEY_LANGUAGE,
            Language.SYSTEM.code
        ) ?: Language.SYSTEM.code
    }

    fun setLanguage(language: String) {
        preferences.edit {
            putString(
                KEY_LANGUAGE,
                language
            )
        }
    }

    // Screen protection

    fun getScreenProtectionEnabled(): Boolean {
        return preferences.getBoolean(
            KEY_SCREEN_PROTECTION,
            false
        )
    }

    fun setScreenProtectionEnabled(enabled: Boolean) {
        preferences.edit {
            putBoolean(
                KEY_SCREEN_PROTECTION,
                enabled
            )
        }
    }

    fun checkAndSetSecureFlags(window: Window) {
        if (getScreenProtectionEnabled()) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        } else {
            window.clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    // Food added count

    fun getFoodAddedCount(): Int {
        return preferences.getInt(
            KEY_FOOD_ADDED_COUNT,
            0
        )
    }

    fun incrementFoodAddedCount(): Int {
        val count = getFoodAddedCount() + 1

        preferences.edit {
            putInt(
                KEY_FOOD_ADDED_COUNT,
                count
            )
        }

        return count
    }
}