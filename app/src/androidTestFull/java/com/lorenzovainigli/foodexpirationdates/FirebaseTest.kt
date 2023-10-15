package com.lorenzovainigli.foodexpirationdates

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import com.lorenzovainigli.foodexpirationdates.view.activity.MainActivity
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class FirebaseTest {

    @get:Rule
    val testRule = createAndroidComposeRule(MainActivity::class.java)

    @Test
    fun firebaseEnabledOnFullBuild(){
        testRule.onNodeWithText("Settings").assertIsDisplayed()
        // Run a UiAutomator command to capture logcat output
        val uiDevice: UiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val logcatOutput: String = uiDevice.executeShellCommand("logcat -d")
        // Check if the logcat output contains the "firebase" string
        assertTrue(logcatOutput.contains("FirebaseApp initialization successful"))
    }
}