package com.lorenzovainigli.foodexpirationdates.view

import android.content.res.Resources
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.lorenzovainigli.foodexpirationdates.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @JvmField
    @Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private lateinit var res: Resources

    @Rule
    @JvmField
    var mGrantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        "android.permission.POST_NOTIFICATIONS"
    )

    @Before
    fun init(){
        res = composeTestRule.activity.resources
    }

    @Test
    fun openApp() {
        composeTestRule.run {
            listOf(
                R.string.app_name, R.string.about_this_app, R.string.list, R.string.settings
            ).forEach {
                onNodeWithText(res.getString(it)).assertIsDisplayed()
            }
        }
    }

    @Test
    fun goToInsertScreen() {
        composeTestRule.run {
            onNodeWithContentDescription(res.getString(R.string.insert)).performClick()
            onNodeWithText(res.getString(R.string.add_item)).assertIsDisplayed()
        }
    }
}