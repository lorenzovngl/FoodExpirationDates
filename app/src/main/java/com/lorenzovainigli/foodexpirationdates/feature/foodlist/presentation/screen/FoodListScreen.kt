package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component.FoodItemsList
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component.FoodListEmptyState
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview.getItemsForPreview
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FoodListScreen(
    items: ImmutableList<FoodCardUiModel>,
    showSnackbar: MutableState<Boolean>? = null,
    isSearchActive: Boolean = false,
    onSearchBarClose: () -> Unit = {},
    onClickDelete: (Int) -> Unit,
    onClickEdit: (Int) -> Unit,
    onFloatingActionButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxSize()
    ) {
        AnimatedVisibility(
            visible = items.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            FoodItemsList(
                items = items,
                showSnackbar = showSnackbar,
                isSearchActive = isSearchActive,
                onSearchBarClose = onSearchBarClose,
                onClickDelete = onClickDelete,
                onClickEdit = onClickEdit
            )
        }
        AnimatedVisibility(
            visible = items.isEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            FoodListEmptyState()
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            onClick = onFloatingActionButtonClick,
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = stringResource(id = R.string.insert)
            )
        }
    }
}

@Preview
@Composable
fun FoodListScreenPreview() {
    FoodExpirationDatesTheme {
        Surface {
            FoodListScreen(
                items = getItemsForPreview(LocalContext.current),
                isSearchActive = true,
                onClickDelete = {},
                onClickEdit = {},
                onFloatingActionButtonClick = {}
            )
        }
    }
}