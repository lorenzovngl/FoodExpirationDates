package com.lorenzovainigli.foodexpirationdates.analytics

enum class AnalyticsEvent(val tag: String) {
    FOOD_ADDED("food_added"),
    FOOD_DELETED("food_deleted")
}