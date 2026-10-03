package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PreferencesRepositoryTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var repository: PreferencesRepository

    @Before
    fun setUp() {
        context = mockk()
        sharedPreferences = mockk()
        editor = mockk()

        every {
            context.getSharedPreferences(
                PreferencesRepository.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE,
            )
        } returns sharedPreferences

        every {
            sharedPreferences.edit()
        } returns editor

        every {
            editor.apply()
        } just Runs

        repository = PreferencesRepository(context)
    }

    @Test
    fun `getDynamicColors returns true when enabled`() {
        every {
            sharedPreferences.getBoolean(
                PreferencesRepository.KEY_DYNAMIC_COLORS,
                false,
            )
        } returns true

        val result = repository.getDynamicColors()

        assertTrue(result)
    }

    @Test
    fun `getDynamicColors returns false when disabled`() {
        every {
            sharedPreferences.getBoolean(
                PreferencesRepository.KEY_DYNAMIC_COLORS,
                false,
            )
        } returns false

        val result = repository.getDynamicColors()

        assertFalse(result)
    }

    @Test
    fun `setDynamicColors stores expected value`() {
        every {
            editor.putBoolean(
                PreferencesRepository.KEY_DYNAMIC_COLORS,
                true,
            )
        } returns editor

        repository.setDynamicColors(true)

        verify {
            editor.putBoolean(
                PreferencesRepository.KEY_DYNAMIC_COLORS,
                true,
            )
        }

        verify {
            editor.apply()
        }
    }

    @Test
    fun `getMonochromeIcons returns true when enabled`() {
        every {
            sharedPreferences.getBoolean(
                PreferencesRepository.KEY_MONOCHROME_ICONS,
                true,
            )
        } returns true

        val result = repository.getMonochromeIcons()

        assertTrue(result)
    }

    @Test
    fun `getMonochromeIcons returns false when disabled`() {
        every {
            sharedPreferences.getBoolean(
                PreferencesRepository.KEY_MONOCHROME_ICONS,
                true,
            )
        } returns false

        val result = repository.getMonochromeIcons()

        assertFalse(result)
    }

    @Test
    fun `setMonochromeIcons stores expected value`() {
        every {
            editor.putBoolean(
                PreferencesRepository.KEY_MONOCHROME_ICONS,
                false,
            )
        } returns editor

        repository.setMonochromeIcons(false)

        verify {
            editor.putBoolean(
                PreferencesRepository.KEY_MONOCHROME_ICONS,
                false,
            )
        }

        verify {
            editor.apply()
        }
    }
}