package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview

import android.content.Context
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.mapper.FoodCardUiModelMapper
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.Calendar
import kotlin.math.min

fun getItemsForPreview(context: Context): ImmutableList<FoodCardUiModel> {
    val items = ArrayList<FoodCardUiModel>()
    val foods = context.resources.getStringArray(R.array.example_foods)
    val quantities = arrayOf(3, 1, 1, 2, 1, 7, 4)
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
                )
            )
        )
    }
    return items.toImmutableList()
}
