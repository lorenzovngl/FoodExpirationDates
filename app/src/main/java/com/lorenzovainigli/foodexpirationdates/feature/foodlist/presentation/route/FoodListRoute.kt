package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel

@Composable
fun FoodListRoute(
    viewModel: FoodListViewModel = hiltViewModel(),
    onEditFoodItem: (Int) -> Unit,
    onAddFoodItem: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoodListScreen(
        items = uiState.items,
        onClickDelete = viewModel::deleteFoodItem,
        onClickEdit = { itemId ->
            onEditFoodItem(itemId)
        },
        onFloatingActionButtonClick = onAddFoodItem
    )
}