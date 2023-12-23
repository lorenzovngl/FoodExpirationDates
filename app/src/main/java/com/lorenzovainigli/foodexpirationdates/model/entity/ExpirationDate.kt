package com.lorenzovainigli.foodexpirationdates.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(tableName = "expiration_dates")
data class ExpirationDate(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "food_name") var foodName: String,
    @ColumnInfo(name = "expiration_date") var expirationDate: Long,
    @ColumnInfo(name = "opening_date") var openingDate: Long? = null,
    @ColumnInfo(name = "time_span_days") var timeSpanDays: Int? = null,
)

fun computeExpirationDate(
    item: ExpirationDate
): Long {
    return computeExpirationDate(item.expirationDate, item.openingDate, item.timeSpanDays)
}

fun computeExpirationDate(
    expirationDate: Long,
    openingDate: Long? = expirationDate,
    timeSpanDays: Int? = 0
): Long {
    val expirationByOpeningDate = openingDate?.let {
        Calendar.getInstance().apply {
            timeInMillis = it
            add(Calendar.DAY_OF_MONTH, timeSpanDays!!)
        }.time.time
    }
    return when (expirationByOpeningDate) {
        null -> expirationDate
        else -> minOf(expirationDate, expirationByOpeningDate)
    }
}