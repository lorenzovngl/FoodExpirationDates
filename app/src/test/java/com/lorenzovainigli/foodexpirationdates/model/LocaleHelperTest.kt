package com.lorenzovainigli.foodexpirationdates.model

import android.content.Context
import android.content.res.Configuration
import androidx.core.os.ConfigurationCompat
import androidx.test.core.app.ApplicationProvider
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper.setLocale
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.Locale
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(RobolectricTestRunner::class)
class LocaleHelperTest {

    private lateinit var originalLocale: Locale

    @Before
    fun setup() {
        // Save the original default locale to restore it after the test,
        // preventing side effects on other tests.
        originalLocale = Locale.getDefault()
    }

    @After
    fun teardown() {
        // Restore the original locale
        Locale.setDefault(originalLocale)
    }

    @Test
    fun `setLocale with specific language creates Context with correct Configuration`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val targetLanguage = Language.ITALIAN.code

        val updatedContext = setLocale(baseContext, targetLanguage)

        val updatedConfig: Configuration = updatedContext.resources.configuration
        val locales = ConfigurationCompat.getLocales(updatedConfig)
        val appliedLocale = locales.get(0)

        assertNotNull(appliedLocale)
        assertEquals(message = "The language should be updated to Italian", expected = targetLanguage, actual = appliedLocale.language)
        assertEquals(message = "The default Locale should also be updated", expected = targetLanguage, actual = Locale.getDefault().language)
    }

    @Test
    fun `setLocale with system language uses default system locale`() {
        val baseContext = ApplicationProvider.getApplicationContext<Context>()
        val systemLanguageCode = Language.SYSTEM.code

        // Force a specific default locale to test against
        val expectedSystemLocale = Locale.ITALIAN
        Locale.setDefault(expectedSystemLocale)

        val updatedContext = setLocale(baseContext, systemLanguageCode)

        val updatedConfig: Configuration = updatedContext.resources.configuration
        val locales = ConfigurationCompat.getLocales(updatedConfig)
        val appliedLocale = locales.get(0)

        assertNotNull(appliedLocale)
        assertEquals(message = "Should use the system default language", expected = expectedSystemLocale.language, actual = appliedLocale.language)
    }

}