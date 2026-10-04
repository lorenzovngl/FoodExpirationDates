package com.lorenzovainigli.foodexpirationdates.model.review

data class ReviewRequest(
    val foodCount: Int,
    val isAutomatic: Boolean,
)