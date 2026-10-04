package com.lorenzovainigli.foodexpirationdates.model.review

import javax.inject.Inject

class ReviewRequestStrategy @Inject constructor(
    private val preferences: ReviewPreferences,
) {

    fun onFoodAdded(): ReviewTriggerResult {
        val count = preferences.incrementFoodAddedCount()
        return ReviewTriggerResult(
            shouldRequestReview = count % REVIEW_INTERVAL == 0,
            foodCount = count,
        )
    }

    companion object {
        private const val REVIEW_INTERVAL = 50
    }
}