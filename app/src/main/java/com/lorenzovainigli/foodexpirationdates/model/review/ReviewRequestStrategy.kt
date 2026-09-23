package com.lorenzovainigli.foodexpirationdates.model.review

import javax.inject.Inject

class ReviewRequestStrategy @Inject constructor(
    private val preferences: ReviewPreferences,
) {

    fun onFoodAdded(): Boolean {
        val count = preferences.incrementFoodAddedCount()
        return count % REVIEW_INTERVAL == 0
    }

    companion object {
        private const val REVIEW_INTERVAL = 50
    }
}