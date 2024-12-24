package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.content.SharedPreferences
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PreferencesRepositoryTest {

    @Test
    fun `getDynamicColors() - SharedPreferences throws exception`() {
        val context = mockk<Context>()
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.getBoolean(any(), any()) } throws RuntimeException("SharedPrefs Error")
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.getDynamicColors(context, "")
        assertFalse(result)
    }

    @Test
    fun `getDynamicColors() - SharedPreferences returns the expected value`() {
        val context = mockk<Context>()
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.getBoolean(any(), any()) } returns true
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.getDynamicColors(context, "")
        assertTrue(result)
    }

    @Test
    fun `getDynamicColors() - Invalid sharedPrefs name`() {
        val context = mockk<Context>()
        val invalidSharedPrefsName = "invalid_name"
        every {
            context.getSharedPreferences(invalidSharedPrefsName, any<Int>())
        } throws RuntimeException("SharedPrefs Error")
        val result = PreferencesRepository.getDynamicColors(context, invalidSharedPrefsName)
        assertFalse(result)
    }

    @Test
    fun `setDynamicColors() - SharedPreferences throws exception`() {
        val context = mockk<Context>()
        val sharedPrefsEditor: SharedPreferences.Editor = mockk(relaxed = true)
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.edit() } returns sharedPrefsEditor
        every { sharedPrefsEditor.putBoolean(any(), any()) } throws RuntimeException("SharedPrefs Error")
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.setDynamicColors(context, "", true)
        assertFalse(result)
    }

    @Test
    fun `setDynamicColors() - SharedPreferences successfully sets the value`() {
        val context = mockk<Context>()
        val sharedPrefsEditor: SharedPreferences.Editor = mockk(relaxed = true)
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.edit() } returns sharedPrefsEditor
        every { sharedPrefsEditor.putBoolean(any(), any()) } returns sharedPrefsEditor
        every { sharedPrefsEditor.apply() } just Runs
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.setDynamicColors(context, "", true)
        assertTrue(result)
    }

    @Test
    fun `setDynamicColors() - Invalid sharedPrefs name`() {
        val context = mockk<Context>()
        val invalidSharedPrefsName = "invalid_name"
        every {
            context.getSharedPreferences(invalidSharedPrefsName, any<Int>())
        } throws RuntimeException("SharedPrefs Error")
        val result = PreferencesRepository.setDynamicColors(context, invalidSharedPrefsName, true)
        assertFalse(result)
    }

    @Test
    fun `getMonochromeIcons() - SharedPreferences throws exception`() {
        val context = mockk<Context>()
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.getBoolean(any(), any()) } throws RuntimeException("SharedPrefs Error")
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.getMonochromeIcons(context, "")
        assertTrue(result)
    }

    @Test
    fun `getMonochromeIcons() - SharedPreferences returns the expected value`() {
        val context = mockk<Context>()
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.getBoolean(any(), any()) } returns false
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.getMonochromeIcons(context, "")
        assertFalse(result)
    }

    @Test
    fun `getMonochromeIcons() - Invalid sharedPrefs name`() {
        val context = mockk<Context>()
        val invalidSharedPrefsName = "invalid_name"
        every {
            context.getSharedPreferences(invalidSharedPrefsName, any<Int>())
        } throws RuntimeException("SharedPrefs Error")
        val result = PreferencesRepository.getMonochromeIcons(context, invalidSharedPrefsName)
        assertTrue(result)
    }

    @Test
    fun `setMonochromeIcons() - SharedPreferences throws exception`() {
        val context = mockk<Context>()
        val sharedPrefsEditor: SharedPreferences.Editor = mockk(relaxed = true)
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.edit() } returns sharedPrefsEditor
        every { sharedPrefsEditor.putBoolean(any(), any()) } throws RuntimeException("SharedPrefs Error")
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.setMonochromeIcons(context, "", true)
        assertFalse(result)
    }

    @Test
    fun `setMonochromeIcons() - SharedPreferences successfully sets the value`() {
        val context = mockk<Context>()
        val sharedPrefsEditor: SharedPreferences.Editor = mockk(relaxed = true)
        val sharedPrefs: SharedPreferences = mockk()
        every { sharedPrefs.edit() } returns sharedPrefsEditor
        every { sharedPrefsEditor.putBoolean(any(), any()) } returns sharedPrefsEditor
        every { sharedPrefsEditor.apply() } just Runs
        every { context.getSharedPreferences(any<String>(), any<Int>()) } returns sharedPrefs
        val result = PreferencesRepository.setMonochromeIcons(context, "", true)
        assertTrue(result)
    }

    @Test
    fun `setMonochromeIcons() - Invalid sharedPrefs name`() {
        val context = mockk<Context>()
        val invalidSharedPrefsName = "invalid_name"
        every {
            context.getSharedPreferences(invalidSharedPrefsName, any<Int>())
        } throws RuntimeException("SharedPrefs Error")
        val result = PreferencesRepository.setMonochromeIcons(context, invalidSharedPrefsName, true)
        assertFalse(result)
    }

}