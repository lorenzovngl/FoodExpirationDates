package com.lorenzovainigli.foodexpirationdates.model.review

import android.content.Context
import androidx.core.content.edit
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReviewPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context,
) : ReviewPreferences {

    private val preferences =
        context.getSharedPreferences(
            PreferencesRepository.SHARED_PREFS_NAME,
            Context.MODE_PRIVATE,
        )

    override fun incrementFoodAddedCount(): Int {
        val count = preferences.getInt(KEY_FOOD_ADDED_COUNT, 0) + 1

        preferences.edit {
            putInt(KEY_FOOD_ADDED_COUNT, count)
        }

        return count
    }

    companion object {
        private const val KEY_FOOD_ADDED_COUNT = "food_added_count"
    }
}