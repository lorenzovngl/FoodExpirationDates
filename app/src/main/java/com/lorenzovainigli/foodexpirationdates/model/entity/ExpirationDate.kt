package com.lorenzovainigli.foodexpirationdates.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Entity(tableName = "expiration_dates")
data class ExpirationDate(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "food_name") var foodName: String,
    @ColumnInfo(name = "expiration_date") var expirationDate: Long,
    @ColumnInfo(name = "opening_date") var openingDate: Long? = null,
    @ColumnInfo(name = "time_span_days") var timeSpanDays: Int? = null,
    @ColumnInfo(name = "quantity", defaultValue = "1") var quantity: Int = 1,
    @ColumnInfo(name = "content_per_package") var contentPerPackage: Int? = null,
    @ColumnInfo(name = "measurement_unit") var measurementUnit: MeasurementUnit? = null,
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
const val CONTENT_PER_PACKAGE_INDEX = 5
const val CONTENT_PER_PACKAGE = "CONTENT_PER_PACKAGE"
const val MEASUREMENT_UNIT_INDEX = 6
const val MEASUREMENT_UNIT = "MEASUREMENT_UNIT"

const val CSV_HEADER = "$FOOD_NAME,$EXPIRATION_DATE,$OPENING_DATE,$TIME_SPAN_DAYS,$QUANTITY,$CONTENT_PER_PACKAGE,$MEASUREMENT_UNIT"

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

fun ExpirationDate.toCSV(): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val zone = ZoneId.systemDefault()

    fun Long.toDateString(): String =
        Instant.ofEpochMilli(this)
            .atZone(zone)
            .toLocalDate()
            .format(formatter)

    return listOf(
        "\"${foodName.replace("\"", "\"\"")}\"",
        expirationDate.toDateString(),
        openingDate?.toDateString() ?: "null",
        timeSpanDays?.toString() ?: "null",
        quantity.toString(),
        contentPerPackage.toString(),
        measurementUnit?.name
    ).joinToString(",")
}

private fun Long.toCsvDate(): String {
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .toString()
}