package com.lorenzovainigli.foodexpirationdates.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expiration_dates")
data class ExpirationDate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "food_name") val foodName: String,
    @ColumnInfo(name = "expiration_date") val expirationDate: Long,
)