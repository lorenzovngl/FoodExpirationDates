package com.lorenzovainigli.foodexpirationdates.model.entity

enum class MeasurementUnit(
    val symbol: String,
    val factor: Int,
) {
    GRAM("g", 1),
    KILOGRAM("kg", 1000),
    MILLILITER("ml", 1),
    CENTILITER("cl", 10),
    LITER("L", 1000),
}