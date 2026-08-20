package com.lorenzovainigli.foodexpirationdates.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview.getItemsForPreview
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MainScreenMenu
import com.lorenzovainigli.foodexpirationdates.view.composable.MyBottomAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun FoodListScreenPreview(
    dynamicColors: Boolean = false
) {
    FoodExpirationDatesTheme(
        dynamicColor = dynamicColors
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Scaffold(
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.app_name),
                        navigationIcon = {},
                        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                        actions = {
                            val isPreview = LocalInspectionMode.current
                            MainScreenMenu(
                                modifier = Modifier.focusProperties {
                                    canFocus = !isPreview
                                },
                                exportTaskSuccess = null,
                                notifyExportTaskDone = null,
                                onSearchClick = { },
                                onExportClick = { },
                                onImportClick = { },
                                onInfoClick = { },
                                onExportErrorDialogDismiss = { }
                            )
                        },
                        monochromeIcons = false
                    )
                },
                bottomBar = {
                    MyBottomAppBar(
                        currentDestination = Screen.MainScreen.route,
                        onNavigationItemClick = {},
                        unreadNewsCount = 1,
                        showPermissionBanner = false
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    FoodListScreen(
                        items = getItemsForPreview(LocalContext.current),
                        onClickDelete = { },
                        onClickEdit = { },
                        onFloatingActionButtonClick = { }
                    )
                }
            }
        }
    }
}