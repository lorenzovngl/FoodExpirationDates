package com.lorenzovainigli.foodexpirationdates.analytics

enum class AnalyticsEvent(val tag: String) {
    FOOD_ADDED("food_added"),
    FOOD_DELETED("food_deleted"),

    ONBOARDING_STARTED("onboarding_started"),
    ONBOARDING_PAGE_VIEWED("onboarding_page_viewed"),
    ONBOARDING_SKIPPED("onboarding_skipped"),
    ONBOARDING_COMPLETED("onboarding_completed"),
    ONBOARDING_NOTIFICATION_REQUESTED("onboarding_notification_requested"),
    ONBOARDING_NOTIFICATION_RESULT("onboarding_notification_result"),
    ONBOARDING_TIME_SAVED("onboarding_time_saved"),

    REVIEW_REQUESTED("review_requested"),
    REVIEW_FLOW_COMPLETED("review_flow_completed"),
    REVIEW_REQUEST_FAILED("review_request_failed"),
    REVIEW_STORE_FALLBACK_OPENED("review_store_fallback_opened"),
}