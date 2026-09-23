package com.lorenzovainigli.foodexpirationdates.model.review

class FakeReviewPreferences(
    initialCount: Int = 0,
) : ReviewPreferences {

    var count = initialCount

    override fun incrementFoodAddedCount(): Int {
        count++
        return count
    }
}