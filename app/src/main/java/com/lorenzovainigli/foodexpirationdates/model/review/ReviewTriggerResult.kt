package com.lorenzovainigli.foodexpirationdates.model.review

data class ReviewTriggerResult(
    val shouldRequestReview: Boolean,
    val foodCount: Int,
)