package com.lorenzovainigli.foodexpirationdates.onboarding.analytics

object OnboardingAnalytics {

    const val VERSION = 1

    const val PARAM_VERSION = "onboarding_version"
    const val PARAM_PAGE_INDEX = "page_index"
    const val PARAM_PAGE_NAME = "page_name"
    const val PARAM_GRANTED = "granted"

    val pageNames = listOf(
        "welcome",
        "manage_food",
        "notification_permission",
        "notification_time",
        "ready"
    )
}