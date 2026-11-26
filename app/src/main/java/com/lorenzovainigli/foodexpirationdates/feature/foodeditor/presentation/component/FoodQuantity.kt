package com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component

enum class QuantityUnit(
    val symbol: String,
    val factor: Int
) {
    GRAM("g", 1),
    KILOGRAM("kg", 1000),
    MILLILITER("ml", 1),
    CENTILITER("cl", 10),
    LITER("L", 1000),
}

data class FoodQuantity(
    val packageCount: Int = 1,
    val contentPerPackage: Int? = null,
    val unit: QuantityUnit = QuantityUnit.GRAM,
) {
    val totalAmount: Long?
        get() = contentPerPackage
            ?.toLong()
            ?.times(packageCount)
            ?.times(unit.factor)

    init {
        require(packageCount >= 1)
        require(contentPerPackage == null || contentPerPackage > 0)
    }
}
