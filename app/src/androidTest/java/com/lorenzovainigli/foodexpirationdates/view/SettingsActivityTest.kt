package com.lorenzovainigli.foodexpirationdates.view

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatRow
import org.junit.Rule
import org.junit.Test
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale


internal class SettingsActivityTest {

    @get:Rule
    val testRule = createAndroidComposeRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.POST_NOTIFICATIONS"
    )

    @Test
    fun changeDateFormat() {
        testRule.run {
            onNodeWithText(testRule.activity.getString(R.string.settings)).performClick()
            onNodeWithTag(testRule.activity.getString(R.string.date_format)).performClick()
            onNodeWithTag(DateFormatDialog).assertIsDisplayed()
            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
            val firstRow = testRule.onAllNodesWithTag(DateFormatRow)[0]
            val currentDate = dateFormat.format(Calendar.getInstance().time)
            firstRow.assertTextContains(currentDate)
            firstRow.performClick()
            onNodeWithTag(testRule.activity.getString(R.string.date_format)).assertTextContains(currentDate)
        }
    }

}