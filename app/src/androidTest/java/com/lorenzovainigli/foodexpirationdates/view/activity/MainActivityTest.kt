package com.lorenzovainigli.foodexpirationdates.view.activity

import android.content.res.Resources
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.composable.FOOD_CARD
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

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

    private fun insertItem(name: String, date: String) {
        composeTestRule.run {
            onNodeWithContentDescription(res.getString(R.string.insert)).run {
                assertIsDisplayed()
                performClick()
            }
            onNodeWithText(res.getString(R.string.food_name)).run {
                assertIsDisplayed()
                performTextInput(name)
            }
            onNodeWithText(res.getString(R.string.expiration_date)).run {
                assertIsDisplayed()
                performClick()
            }
            onNodeWithText(date, true).run {
                assertIsDisplayed()
                performClick()
            }
            onNodeWithTag("Insert date").run {
                assertIsDisplayed()
                performClick()
            }
            onNodeWithTag("Insert item").run {
                assertIsDisplayed()
                performClick()
            }
        }
    }

    private fun clear(){
        composeTestRule.run {
            onAllNodesWithTag("Delete item").apply {
                fetchSemanticsNodes().forEachIndexed { _, _ ->
                    get(0).performClick()
                }
            }
            onNodeWithTag(FOOD_CARD).assertDoesNotExist()
        }
    }

    fun formatDateForDatePicker(
        calendar: Calendar = Calendar.getInstance()
    ): String {
        val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    @Test
    fun insertItemTest() {
        val name = "Test"
        insertItem(
            name = name,
            date = formatDateForDatePicker()
        )
        composeTestRule.run {
            arrayOf(
                name, res.getString(R.string.today)
            ).forEach {
                onNodeWithText(it).assertIsDisplayed()
            }
        }
        clear()
    }

    @Test
    fun updateItemTest() {
        val name = "Test"
        val updatedName = "Test updated"
        insertItem(
            name = name,
            date = formatDateForDatePicker()
        )
        composeTestRule.run {
            onNodeWithText(name).performClick()
            waitForIdle()
            onNodeWithText(res.getString(R.string.food_name)).run {
                performTextReplacement(updatedName)
            }
            onNodeWithTag("Insert item").run {
                performClick()
            }
            waitForIdle()
            onNodeWithText(updatedName).assertIsDisplayed()
        }
        clear()
    }

    @Test
    fun deleteItemTest() {
        val name = "Test"
        insertItem(
            name = name,
            date = formatDateForDatePicker()
        )
        clear()
    }

    @Test
    fun insertMultipleItemsTest() {
        for (i in 0 until 3) {
            insertItem(
                name = "Test item $i",
                date = formatDateForDatePicker(Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, i-1)
                })
            )
        }
        composeTestRule.run {
            for (i in 0 until 3) {
                onNodeWithText("Test item $i").assertExists()
            }
            arrayOf(
                R.string.yesterday, R.string.today, R.string.tomorrow
            ).forEach {
                onNodeWithText(res.getString(it)).assertIsDisplayed()
            }
        }
        clear()
    }
}
