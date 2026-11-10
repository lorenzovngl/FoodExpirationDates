package com.lorenzovainigli.foodexpirationdates.onboarding.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.ManageFoodPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.NotificationPermissionPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.NotificationTimePage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.ReadyPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.WelcomePage
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews

@Preview(name = "01 - Welcome", group= "Default", showBackground = true)
@LanguagePreviews
@Composable
private fun WelcomePreview() {
    PreviewContainer {
        WelcomePage()
    }
}

@Preview(name = "02 - Manage Food", group= "Default", showBackground = true)
@LanguagePreviews
@Composable
private fun ManageFoodPreview() {
    PreviewContainer {
        ManageFoodPage()
    }
}

@Preview(name = "03 - Notifications Disabled", group= "Default", showBackground = true)
@LanguagePreviews
@Composable
private fun NotificationsPreview() {
    PreviewContainer {
        NotificationPermissionPage(
            notificationsEnabled = false,
            onEnableNotifications = {}
        )
    }
}

@Preview(name = "04 - Reminder Time", group= "Default", showBackground = true)
@LanguagePreviews
@Composable
private fun ReminderTimePreview() {
    PreviewContainer {
        NotificationTimePage(
            initialHour = 11,
            initialMinute = 0,
            onTimeSelected = { _, _ -> }
        )
    }
}

@Preview(name = "05 - Ready", group= "Default", showBackground = true)
@LanguagePreviews
@Composable
private fun ReadyPreview() {
    PreviewContainer {
        ReadyPage()
    }
}

@Composable
private fun PreviewContainer(
    content: @Composable () -> Unit
) {
    FoodExpirationDatesTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                content()
            }
        }
    }
}
