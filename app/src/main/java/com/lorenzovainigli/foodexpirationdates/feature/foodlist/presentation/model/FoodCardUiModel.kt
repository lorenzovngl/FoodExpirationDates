package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model


enum class QuantityVisualType {
    PACKAGE,
    WEIGHT,
    LIQUID,
}

data class FoodCardUiModel(
    val id: Int,
    val foodName: String,
    val expirationText: String,
    val daysUntilExpiration: Int,
    val isOpened: Boolean,
    val expirationStatus: ExpirationStatus,
    val packageCount: Int,
    val packageInfo: String?,
    val quantityVisualType: QuantityVisualType
)
