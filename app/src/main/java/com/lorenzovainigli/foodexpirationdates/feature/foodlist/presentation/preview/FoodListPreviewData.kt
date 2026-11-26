package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview

import android.content.Context
import androidx.compose.runtime.Composable
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.mapper.FoodCardUiModelMapper
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.MeasurementUnit
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.Calendar
import kotlin.math.min

@Composable
fun getItemsForPreview(context: Context): ImmutableList<FoodCardUiModel> {
    val items = ArrayList<FoodCardUiModel>()
    val foods = context.resources.getStringArray(R.array.example_foods)
    val quantities = arrayOf(3, 1, 1, 2, 1, 7, 4)
    val contentsPerPackage = arrayOf(null, 500, 1, 250, 125, 1, 750)
    val units = arrayOf(
        null,
        MeasurementUnit.GRAM,
        MeasurementUnit.LITER,
        MeasurementUnit.GRAM,
        MeasurementUnit.GRAM,
        MeasurementUnit.KILOGRAM,
        MeasurementUnit.GRAM,
    )
    val daysLeft = arrayOf(-1, 0, 1, 3, 7, 10, 30)
    val mapper = FoodCardUiModelMapper(context)
    for (i in 0 until min(foods.size, daysLeft.size)) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, daysLeft[i])
        items.add(
            mapper.map(
                ExpirationDate(
                    id = i,
                    foodName = foods[i],
                    expirationDate = cal.time.time,
                    quantity = quantities[i],
                    contentPerPackage = contentsPerPackage[i],
                    measurementUnit = units[i]
                ),
                dateFormat = "d MMM"
            )
        )
    }
    return items.toImmutableList()
}
