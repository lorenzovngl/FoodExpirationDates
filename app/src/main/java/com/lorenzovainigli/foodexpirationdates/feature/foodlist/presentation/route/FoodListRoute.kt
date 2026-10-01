package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel

@Composable
fun FoodListRoute(
    viewModel: FoodListViewModel = hiltViewModel(),
    showSnackbar: MutableState<Boolean>?,
    onEditFoodItem: (Int) -> Unit,
    onAddFoodItem: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoodListScreen(
        items = uiState.items,
        showSnackbar = showSnackbar,
        onClickDelete = viewModel::deleteFoodItem,
        onClickEdit = { itemId ->
            onEditFoodItem(itemId)
        },
        onFloatingActionButtonClick = onAddFoodItem
    )
}