package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class PreferencesRepositoryTest {

    private val testSharedPrefsName = "test_shared_prefs"
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun tearDown() {
        context.deleteSharedPreferences(testSharedPrefsName)
    }

    @Test
    fun userDateFormatTest() {
        val sharedPrefs =
            context.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE)
        // Test the default value
        assertEquals(
            sharedPrefs.getString(
                PreferencesRepository.keyDateFormat,
                PreferencesRepository.getAvailOtherDateFormats()[0]
            ),
            PreferencesRepository.getAvailOtherDateFormats()[0]
        )
        // Test the setter
        PreferencesRepository.setUserDateFormat(
            context,
            testSharedPrefsName,
            PreferencesRepository.getAvailOtherDateFormats()[1]
        )
        assertEquals(
            sharedPrefs.getString(
                PreferencesRepository.keyDateFormat,
                PreferencesRepository.getAvailOtherDateFormats()[0]
            ),
            PreferencesRepository.getAvailOtherDateFormats()[1]
        )
        // Test the getter
        assertEquals(
            PreferencesRepository.getUserDateFormat(context, testSharedPrefsName),
            PreferencesRepository.getAvailOtherDateFormats()[1]
        )
    }

    @Test
    fun userNotificationTimeHourTest() {
        val sharedPrefs =
            context.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE)
        // Test the default value
        assertEquals(sharedPrefs.getInt(PreferencesRepository.keyNotificationTimeHour, 11), 11)
        // Test the setter
        PreferencesRepository.setUserNotificationTime(context, testSharedPrefsName, 12, 30)
        assertEquals(sharedPrefs.getInt(PreferencesRepository.keyNotificationTimeHour, 11), 12)
        assertEquals(sharedPrefs.getInt(PreferencesRepository.keyNotificationTimeMinute, 11), 30)
        // Test the getter
        assertEquals(
            PreferencesRepository.getUserNotificationTimeHour(
                context = context,
                sharedPrefs = testSharedPrefsName
            ), 12
        )
        assertEquals(
            PreferencesRepository.getUserNotificationTimeMinute(
                context = context,
                sharedPrefs = testSharedPrefsName
            ), 30
        )
    }

    @Test
    fun userThemeMode() {
        val sharedPrefs =
            context.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE)
        // Test the default value
        assertEquals(
            sharedPrefs.getInt(
                PreferencesRepository.keyThemeMode,
                PreferencesRepository.Companion.ThemeMode.SYSTEM.ordinal
            ),
            PreferencesRepository.Companion.ThemeMode.SYSTEM.ordinal
        )
        // Test the setter
        PreferencesRepository.setThemeMode(
            context = context,
            sharedPrefs = testSharedPrefsName,
            themeMode = PreferencesRepository.Companion.ThemeMode.DARK
        )
        assertEquals(
            sharedPrefs.getInt(
                PreferencesRepository.keyThemeMode,
                PreferencesRepository.Companion.ThemeMode.SYSTEM.ordinal
            ),
            PreferencesRepository.Companion.ThemeMode.DARK.ordinal
        )
        // Test the getter
        assertEquals(
            PreferencesRepository.getThemeMode(
                context = context,
                sharedPrefs = testSharedPrefsName
            ),
            PreferencesRepository.Companion.ThemeMode.DARK.ordinal
        )
    }

    @Test
    fun userDynamicColors() {
        val sharedPrefs =
            context.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE)
        // Test the default value
        assertEquals(
            sharedPrefs.getBoolean(
                PreferencesRepository.keyDynamicColors,
                false
            ),
            false
        )
        // Test the setter
        PreferencesRepository.setDynamicColors(
            context = context,
            sharedPrefs = testSharedPrefsName,
            dynamicColorsEnabled = true
        )
        assertEquals(
            sharedPrefs.getBoolean(
                PreferencesRepository.keyDynamicColors,
                false
            ),
            true
        )
        // Test the getter
        assertEquals(
            PreferencesRepository.getDynamicColors(
                context = context,
                sharedPrefs = testSharedPrefsName
            ),
            true
        )
    }

}