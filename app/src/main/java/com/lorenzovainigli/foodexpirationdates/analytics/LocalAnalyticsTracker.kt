package com.lorenzovainigli.foodexpirationdates.analytics

import androidx.compose.runtime.staticCompositionLocalOf

val LocalAnalyticsTracker = staticCompositionLocalOf<AnalyticsTracker> {
    error("AnalyticsTracker not provided")
}