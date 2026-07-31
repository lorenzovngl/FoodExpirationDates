package com.lorenzovainigli.foodexpirationdates.analytics

import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

interface AnalyticsTracker {
    fun logEvent(event: AnalyticsEvent/*, params: Map<String, Any?> = emptyMap()*/)
    fun logScreenView(screen: Screen)
}