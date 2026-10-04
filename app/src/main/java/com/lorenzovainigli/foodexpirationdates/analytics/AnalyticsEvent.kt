package com.lorenzovainigli.foodexpirationdates.analytics

enum class AnalyticsEvent(val tag: String) {
    FOOD_ADDED("food_added"),
    FOOD_DELETED("food_deleted"),

    REVIEW_REQUESTED("review_requested"),
    REVIEW_FLOW_COMPLETED("review_flow_completed"),
    REVIEW_REQUEST_FAILED("review_request_failed"),
    REVIEW_STORE_FALLBACK_OPENED("review_store_fallback_opened"),
}