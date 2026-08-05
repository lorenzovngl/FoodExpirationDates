package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FoodListUiState(
    val items: ImmutableList<FoodCardUiModel> = persistentListOf(),
    val isLoading: Boolean = true,
)