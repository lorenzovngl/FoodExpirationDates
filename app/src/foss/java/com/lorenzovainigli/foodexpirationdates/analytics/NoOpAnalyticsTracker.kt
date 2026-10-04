package com.lorenzovainigli.foodexpirationdates.analytics

import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import jakarta.inject.Inject

class NoOpAnalyticsTracker @Inject constructor() : AnalyticsTracker {

    override fun logEvent(event: AnalyticsEvent, params: Map<String, Any?>) {
        // No analytics in the FOSS flavor.
    }

    override fun logScreenView(screen: Screen) {
        // No analytics in the FOSS flavor.
    }
}