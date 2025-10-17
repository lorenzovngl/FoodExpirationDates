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
    @ColumnInfo(name = "quantity", defaultValue = "1") var quantity: Int = 1,
    @ColumnInfo(name = "date_added", defaultValue = "0") var dateAdded: Long = System.currentTimeMillis()
)

const val FOOD_NAME_INDEX = 0
const val FOOD_NAME = "FOOD_NAME"
const val EXPIRATION_DATE_INDEX = 1
const val EXPIRATION_DATE = "EXPIRATION_DATE"
const val OPENING_DATE_INDEX = 2
const val OPENING_DATE = "OPENING_DATE"
const val TIME_SPAN_DAYS_INDEX = 3
const val TIME_SPAN_DAYS = "TIME_SPAN_DAYS"
const val QUANTITY_INDEX = 4
const val QUANTITY = "QUANTITY"
const val DATE_ADDED_INDEX = 5
const val DATE_ADDED = "DATE_ADDED"

const val CSV_HEADER = "$FOOD_NAME,$EXPIRATION_DATE,$OPENING_DATE,$TIME_SPAN_DAYS,$QUANTITY,$DATE_ADDED"

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

fun ExpirationDate.toCSV(): String{
    return "$foodName,$expirationDate,$openingDate,$timeSpanDays,$quantity,$dateAdded"
}