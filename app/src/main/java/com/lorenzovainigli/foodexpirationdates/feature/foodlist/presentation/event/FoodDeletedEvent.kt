package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.event

import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate

data class FoodDeletedEvent(
    val item: ExpirationDate
)
