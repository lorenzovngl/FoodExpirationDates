package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview.getItemsForPreview
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MySearchBar
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FoodItemsList(
    items: ImmutableList<FoodCardUiModel>,
    showSnackbar: MutableState<Boolean>?,
    isSearchActive: Boolean,
    onSearchBarClose: () -> Unit = {},
    onClickDelete: (Int) -> Unit,
    onClickEdit: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    Column {
        AnimatedVisibility(visible = isSearchActive) {
            MySearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onClose = {
                    searchQuery = ""
                    onSearchBarClose()
                }
            )
        }
        LazyColumn {
            items(items, key = { it.id }) { item ->
                AnimatedVisibility(
                    modifier = Modifier.animateItem(),
                    visible = item.foodName.contains(searchQuery, ignoreCase = true),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    FoodCard(
                        item = item,
                        onClickEdit = {
                            onClickEdit(item.id)
                        },
                        onClickDelete = {
                            showSnackbar?.value = true
                            onClickDelete(item.id)
                        }
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}

@Composable
@PreviewLightDark
fun FoodItemsListPreview() {
    FoodExpirationDatesTheme {
        Surface {
            FoodItemsList(
                items = getItemsForPreview(LocalContext.current),
                showSnackbar = null,
                isSearchActive = true,
                onClickDelete = {},
                onClickEdit = {}
            )
        }
    }
}