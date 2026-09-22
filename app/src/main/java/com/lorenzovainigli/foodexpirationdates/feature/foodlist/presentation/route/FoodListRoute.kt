package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel

@Composable
fun FoodListRoute(
    viewModel: FoodListViewModel = hiltViewModel(),
    viewModelOld: ExpirationDatesViewModel = hiltViewModel(),
    onEditFoodItem: (Int) -> Unit,
    onAddFoodItem: () -> Unit,
    onRequestReview: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModelOld.requestReview.collect {
            onRequestReview()
        }
    }

    FoodListScreen(
        items = uiState.items,
        onClickDelete = viewModel::deleteFoodItem,
        onClickEdit = { itemId ->
            onEditFoodItem(itemId)
        },
        onFloatingActionButtonClick = onAddFoodItem
    )
}