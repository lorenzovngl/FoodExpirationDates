package com.lorenzovainigli.foodexpirationdates.view.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.route.SettingsRoute
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.Navigation
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.screen.FoodListScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview.getItemsForPreview

class DefaultPreviews {
    @PreviewLightDark
    @Composable
    fun MainScreenPreview() {
        FoodExpirationDatesTheme(
            dynamicColor = false
        ) {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                Navigation(navController = navController, showSnackbar = showSnackbar, startDestination = Screen.AboutScreen.route)
            }
        }
    }

    @PreviewLightDark
    @PreviewDynamicColors
    @Composable
    fun FoodListScreenDynamicColorsPreview() {
        FoodExpirationDatesTheme {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
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

    @PreviewLightDark
    @Composable
    fun InsertScreenPreview() {
        FoodExpirationDatesTheme(
            dynamicColor = false
        ) {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                InsertScreen(navController = navController)
            }
        }
    }

    @PreviewLightDark
    @Composable
    fun SettingsScreenPreview() {
        FoodExpirationDatesTheme(
            dynamicColor = false
        ) {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                SettingsRoute()
            }
        }
    }

    @PreviewLightDark
    @Composable
    fun InfoScreenPreview() {
        FoodExpirationDatesTheme(
            dynamicColor = false
        ) {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                InfoScreen()
            }
        }
    }

}