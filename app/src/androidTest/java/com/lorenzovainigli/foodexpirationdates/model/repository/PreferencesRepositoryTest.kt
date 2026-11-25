package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

internal class PreferencesRepositoryTest {

    private val testSharedPrefsName = "test_shared_prefs"

    private lateinit var context: Context
    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var repository: PreferencesRepository

    @Before
    fun setUp() {
        val baseContext =
            InstrumentationRegistry.getInstrumentation().context

        context = object : ContextWrapper(baseContext) {
            override fun getSharedPreferences(
                name: String,
                mode: Int
            ): SharedPreferences {
                return super.getSharedPreferences(
                    testSharedPrefsName,
                    mode
                )
            }
        }

        sharedPrefs = context.getSharedPreferences(
            testSharedPrefsName,
            Context.MODE_PRIVATE
        )

        sharedPrefs.edit(commit = true) {
            clear()
        }

        repository = PreferencesRepository(context)
    }

    @After
    fun tearDown() {
        context.deleteSharedPreferences(testSharedPrefsName)
    }

    @Test
    fun defaultPreferencesTest() {
        assertEquals("d MMM", repository.getUserDateFormat())
        assertEquals(11, repository.getUserNotificationTimeHour())
        assertEquals(0, repository.getUserNotificationTimeMinute())
        assertEquals(ThemeMode.SYSTEM, repository.getThemeMode())
        assertEquals(TopBarFont.NORMAL, repository.getTopBarFont())
        assertFalse(repository.getDynamicColors())
        assertTrue(repository.getMonochromeIcons())
        assertEquals(Language.SYSTEM.code, repository.getLanguage())
        assertFalse(repository.getScreenProtectionEnabled())
        assertEquals(0, repository.getFoodAddedCount())
        assertEquals(0, repository.getOnboardingCompletedVersion())
        assertFalse(repository.isOnboardingCompleted())
    }

    @Test
    fun userDateFormatTest() {
        val dateFormat = "yyyy-MM-dd"

        repository.setUserDateFormat(dateFormat)

        assertEquals(dateFormat, repository.getUserDateFormat())
        assertEquals(
            dateFormat,
            sharedPrefs.getString(
                PreferencesRepository.KEY_DATE_FORMAT,
                null
            )
        )
    }

    @Test
    fun availableOtherDateFormatsTest() {
        val expected = listOf(
            "d MMM",
            "d MMM yyyy",
            "d MMMM yyyy",
            "yyyy-MM-dd",
            "MM-dd",
            "d/MM",
            "d/MM/yyyy"
        )

        assertEquals(expected, repository.getAvailOtherDateFormats())
    }

    @Test
    fun availableLocaleDateFormatsTest() {
        val formats = repository.getAvailLocaleDateFormats()

        assertEquals(2, formats.size)
        assertTrue(formats.all { it.isNotBlank() })
    }

    @Test
    fun userNotificationTimeTest() {
        repository.setUserNotificationTime(
            hour = 12,
            minute = 30
        )

        assertEquals(12, repository.getUserNotificationTimeHour())
        assertEquals(30, repository.getUserNotificationTimeMinute())

        assertEquals(
            12,
            sharedPrefs.getInt(
                PreferencesRepository.KEY_NOTIFICATION_TIME_HOUR,
                -1
            )
        )

        assertEquals(
            30,
            sharedPrefs.getInt(
                PreferencesRepository.KEY_NOTIFICATION_TIME_MINUTE,
                -1
            )
        )
    }

    @Test
    fun notificationTimeBoundariesTest() {
        repository.setUserNotificationTime(0, 0)

        assertEquals(0, repository.getUserNotificationTimeHour())
        assertEquals(0, repository.getUserNotificationTimeMinute())

        repository.setUserNotificationTime(23, 59)

        assertEquals(23, repository.getUserNotificationTimeHour())
        assertEquals(59, repository.getUserNotificationTimeMinute())
    }

    @Test
    fun userThemeModeTest() {
        ThemeMode.entries.forEach { themeMode ->
            repository.setThemeMode(themeMode)

            assertEquals(themeMode, repository.getThemeMode())

            assertEquals(
                themeMode.ordinal,
                sharedPrefs.getInt(
                    PreferencesRepository.KEY_THEME_MODE,
                    -1
                )
            )
        }
    }

    @Test
    fun invalidThemeModeTest() {
        listOf(-1, ThemeMode.entries.size, Int.MAX_VALUE)
            .forEach { invalidOrdinal ->

                sharedPrefs.edit(commit = true) {
                    putInt(
                        PreferencesRepository.KEY_THEME_MODE,
                        invalidOrdinal
                    )
                }

                assertEquals(
                    ThemeMode.SYSTEM,
                    repository.getThemeMode()
                )
            }
    }

    @Test
    fun topBarFontTest() {
        TopBarFont.entries.forEach { font ->
            repository.setTopBarFont(font)

            assertEquals(font, repository.getTopBarFont())

            assertEquals(
                font.ordinal,
                sharedPrefs.getInt(
                    PreferencesRepository.KEY_TOP_BAR_FONT,
                    -1
                )
            )
        }
    }

    @Test
    fun invalidTopBarFontTest() {
        listOf(-1, TopBarFont.entries.size, Int.MAX_VALUE)
            .forEach { invalidOrdinal ->

                sharedPrefs.edit(commit = true) {
                    putInt(
                        PreferencesRepository.KEY_TOP_BAR_FONT,
                        invalidOrdinal
                    )
                }

                assertEquals(
                    TopBarFont.NORMAL,
                    repository.getTopBarFont()
                )
            }
    }

    @Test
    fun dynamicColorsTest() {
        repository.setDynamicColors(true)
        assertTrue(repository.getDynamicColors())

        repository.setDynamicColors(false)
        assertFalse(repository.getDynamicColors())
    }

    @Test
    fun monochromeIconsTest() {
        repository.setMonochromeIcons(false)
        assertFalse(repository.getMonochromeIcons())

        repository.setMonochromeIcons(true)
        assertTrue(repository.getMonochromeIcons())
    }

    @Test
    fun screenProtectionTest() {
        repository.setScreenProtectionEnabled(true)
        assertTrue(repository.getScreenProtectionEnabled())

        repository.setScreenProtectionEnabled(false)
        assertFalse(repository.getScreenProtectionEnabled())
    }

    @Test
    fun incrementFoodAddedCountTest() {
        assertEquals(0, repository.getFoodAddedCount())

        repeat(10) { index ->
            val expected = index + 1

            assertEquals(
                expected,
                repository.incrementFoodAddedCount()
            )

            assertEquals(
                expected,
                repository.getFoodAddedCount()
            )
        }
    }

    @Test
    fun completeOnboardingTest() {
        assertFalse(repository.isOnboardingCompleted())

        repository.completeOnboarding()

        assertTrue(repository.isOnboardingCompleted())

        assertEquals(
            PreferencesRepository.CURRENT_ONBOARDING_VERSION,
            repository.getOnboardingCompletedVersion()
        )
    }

    @Test
    fun outdatedOnboardingVersionTest() {
        sharedPrefs.edit(commit = true) {
            putInt(
                PreferencesRepository.KEY_ONBOARDING_COMPLETED_VERSION,
                PreferencesRepository.CURRENT_ONBOARDING_VERSION - 1
            )
        }

        assertFalse(repository.isOnboardingCompleted())
    }

    @Test
    fun futureOnboardingVersionTest() {
        sharedPrefs.edit(commit = true) {
            putInt(
                PreferencesRepository.KEY_ONBOARDING_COMPLETED_VERSION,
                PreferencesRepository.CURRENT_ONBOARDING_VERSION + 1
            )
        }

        assertTrue(repository.isOnboardingCompleted())
    }

    @Test
    fun migrateLegacyPreferencesTest() {
        repository.setUserDateFormat("yyyy-MM-dd")

        assertFalse(repository.isOnboardingCompleted())

        repository.migrateOnboardingPreferences()

        assertTrue(repository.isOnboardingCompleted())

        assertEquals(
            PreferencesRepository.CURRENT_ONBOARDING_VERSION,
            repository.getOnboardingCompletedVersion()
        )

        assertEquals(
            "yyyy-MM-dd",
            repository.getUserDateFormat()
        )
    }

    @Test
    fun migrateAlreadyCompletedOnboardingTest() {
        repository.completeOnboarding()

        repository.migrateOnboardingPreferences()

        assertTrue(repository.isOnboardingCompleted())

        assertEquals(
            PreferencesRepository.CURRENT_ONBOARDING_VERSION,
            repository.getOnboardingCompletedVersion()
        )
    }

    @Test
    fun migrationPreservesExistingOnboardingVersionTest() {
        val version =
            PreferencesRepository.CURRENT_ONBOARDING_VERSION + 1

        sharedPrefs.edit(commit = true) {
            putInt(
                PreferencesRepository.KEY_ONBOARDING_COMPLETED_VERSION,
                version
            )
        }

        repository.migrateOnboardingPreferences()

        assertEquals(
            version,
            repository.getOnboardingCompletedVersion()
        )
    }

    @Test
    fun preferencesPersistenceTest() {
        repository.setUserDateFormat("yyyy-MM-dd")
        repository.setThemeMode(ThemeMode.DARK)
        repository.setTopBarFont(TopBarFont.BOLD)
        repository.setDynamicColors(true)
        repository.setMonochromeIcons(false)
        repository.setLanguage("it")
        repository.setScreenProtectionEnabled(true)
        repository.setUserNotificationTime(18, 30)
        repository.incrementFoodAddedCount()
        repository.completeOnboarding()

        val newRepository = PreferencesRepository(context)

        assertEquals("yyyy-MM-dd", newRepository.getUserDateFormat())
        assertEquals(ThemeMode.DARK, newRepository.getThemeMode())
        assertEquals(TopBarFont.BOLD, newRepository.getTopBarFont())
        assertTrue(newRepository.getDynamicColors())
        assertFalse(newRepository.getMonochromeIcons())
        assertEquals("it", newRepository.getLanguage())
        assertTrue(newRepository.getScreenProtectionEnabled())
        assertEquals(18, newRepository.getUserNotificationTimeHour())
        assertEquals(30, newRepository.getUserNotificationTimeMinute())
        assertEquals(1, newRepository.getFoodAddedCount())
        assertTrue(newRepository.isOnboardingCompleted())
    }

    @Test
    fun settingsFlowInitialStateTest() = runTest {
        repository.settingsFlow.test {
            val state = awaitItem()

            assertEquals("d MMM", state.dateFormat)
            assertEquals(11, state.notificationHour)
            assertEquals(0, state.notificationMinute)
            assertEquals(ThemeMode.SYSTEM, state.themeMode)
            assertEquals(TopBarFont.NORMAL, state.topBarFont)
            assertFalse(state.dynamicColorsEnabled)
            assertTrue(state.monochromeIconsEnabled)
            assertEquals(Language.SYSTEM, state.language)
            assertFalse(state.screenProtectionEnabled)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun settingsFlowUpdatesTest() = runTest {
        repository.settingsFlow.test {
            awaitItem()

            repository.setUserDateFormat("yyyy-MM-dd")
            assertEquals("yyyy-MM-dd", awaitItem().dateFormat)

            repository.setThemeMode(ThemeMode.DARK)
            assertEquals(ThemeMode.DARK, awaitItem().themeMode)

            repository.setTopBarFont(TopBarFont.BOLD)
            assertEquals(TopBarFont.BOLD, awaitItem().topBarFont)

            repository.setDynamicColors(true)
            assertTrue(awaitItem().dynamicColorsEnabled)

            repository.setMonochromeIcons(false)
            assertFalse(awaitItem().monochromeIconsEnabled)

            repository.setLanguage("it")
            assertEquals(
                Language.fromCode("it"),
                awaitItem().language
            )

            repository.setScreenProtectionEnabled(true)
            assertTrue(awaitItem().screenProtectionEnabled)

            repository.setUserNotificationTime(18, 30)

            val state = awaitItem()

            assertEquals(18, state.notificationHour)
            assertEquals(30, state.notificationMinute)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun settingsFlowExternalChangeTest() = runTest {
        repository.settingsFlow.test {
            awaitItem()

            sharedPrefs.edit(commit = true) {
                putBoolean(
                    PreferencesRepository.KEY_DYNAMIC_COLORS,
                    true
                )
            }

            assertTrue(awaitItem().dynamicColorsEnabled)

            cancelAndIgnoreRemainingEvents()
        }
    }

}