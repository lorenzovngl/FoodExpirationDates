package com.lorenzovainigli.foodexpirationdates.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expiration_dates")
data class ExpirationDate(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "food_name") var foodName: String,
    @ColumnInfo(name = "expiration_date") var expirationDate: Long,
    @ColumnInfo(name = "opening_date") var openingDate: Long? = null,
    @ColumnInfo(name = "time_span_days") var timeSpanDays: Int? = null,
)