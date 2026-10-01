package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel

@Composable
fun FoodListRoute(
    viewModel: FoodListViewModel = hiltViewModel(),
    showSnackbar: MutableState<Boolean>?,
    isSearchActive: Boolean,
    onEditFoodItem: (Int) -> Unit,
    onAddFoodItem: () -> Unit,
    onSearchBarClose: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FoodListScreen(
        items = uiState.items,
        showSnackbar = showSnackbar,
        isSearchActive = isSearchActive,
        onClickDelete = viewModel::deleteFoodItem,
        onClickEdit = { itemId ->
            onEditFoodItem(itemId)
        },
        onFloatingActionButtonClick = onAddFoodItem,
        onSearchBarClose = onSearchBarClose
    )
}