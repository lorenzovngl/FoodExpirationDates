package com.lorenzovainigli.foodexpirationdates.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsEvent
import com.lorenzovainigli.foodexpirationdates.onboarding.analytics.OnboardingAnalytics
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.ManageFoodPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.NotificationPermissionPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.NotificationTimePage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.ReadyPage
import com.lorenzovainigli.foodexpirationdates.onboarding.pages.WelcomePage
import com.lorenzovainigli.foodexpirationdates.util.areNotificationsEnabled
import com.lorenzovainigli.onboarding.Onboarding

@Composable
fun AppOnboardingScreen(
    initialNotificationHour: Int,
    initialNotificationMinute: Int,
    onNotificationTimeSelected: (Int, Int) -> Unit,
    onComplete: () -> Unit,
    onAnalyticsEvent: (
        AnalyticsEvent,
        Map<String, Any?>
    ) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current

    val viewedPages = remember {
        mutableSetOf<Int>()
    }

    fun track(event: AnalyticsEvent, params: Map<String, Any?> = emptyMap()) {
        onAnalyticsEvent(event, params + (OnboardingAnalytics.PARAM_VERSION to OnboardingAnalytics.VERSION))
    }

    LaunchedEffect(Unit) {
        track(AnalyticsEvent.ONBOARDING_STARTED)
    }

    var notificationsEnabled by remember {
        mutableStateOf(areNotificationsEnabled(context))
    }

    LifecycleResumeEffect(Unit) {
        notificationsEnabled = areNotificationsEnabled(context)
        onPauseOrDispose {}
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationsEnabled = areNotificationsEnabled(context)
        track(
            AnalyticsEvent.ONBOARDING_NOTIFICATION_RESULT,
            mapOf(OnboardingAnalytics.PARAM_GRANTED to granted)
        )
    }

    Onboarding(
        pageCount = 5,
        onComplete = {
            track(AnalyticsEvent.ONBOARDING_COMPLETED)
            onComplete()
        },
        onSkip = { page ->
            track(
                AnalyticsEvent.ONBOARDING_SKIPPED,
                mapOf(
                    OnboardingAnalytics.PARAM_PAGE_INDEX to page,
                    OnboardingAnalytics.PARAM_PAGE_NAME to OnboardingAnalytics.pageNames[page]
                )
            )
            onComplete()
        },
        onPageViewed = { page ->
            if (page in OnboardingAnalytics.pageNames.indices && viewedPages.add(page)) {
                track(
                    AnalyticsEvent.ONBOARDING_PAGE_VIEWED,
                    mapOf(
                        OnboardingAnalytics.PARAM_PAGE_INDEX to page,
                        OnboardingAnalytics.PARAM_PAGE_NAME to OnboardingAnalytics.pageNames[page]
                    )
                )
            }
        },
        nextLabel = stringResource(R.string.onboarding_next),
        backLabel = stringResource(R.string.onboarding_back),
        skipLabel = stringResource(R.string.onboarding_skip),
        completeLabel = stringResource(R.string.onboarding_get_started)
    ) { page ->
        when (page) {
            0 -> WelcomePage()

            1 -> ManageFoodPage()

            2 -> NotificationPermissionPage(
                notificationsEnabled = notificationsEnabled,
                onEnableNotifications = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        track(
                            AnalyticsEvent.ONBOARDING_NOTIFICATION_REQUESTED
                        )
                        permissionLauncher.launch(
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                    } else {
                        notificationsEnabled = areNotificationsEnabled(context)
                    }
                }
            )

            3 -> NotificationTimePage(
                initialHour = initialNotificationHour,
                initialMinute = initialNotificationMinute,
                onTimeSelected = { hour, minute ->
                    onNotificationTimeSelected(hour, minute)
                    track(AnalyticsEvent.ONBOARDING_TIME_SAVED)
                }
            )

            4 -> ReadyPage()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppOnboardingScreenPreview() {
    MaterialTheme {
        AppOnboardingScreen(
            initialNotificationHour = 11,
            initialNotificationMinute = 0,
            onNotificationTimeSelected = { _, _ -> },
            onComplete = {}
        )
    }
}