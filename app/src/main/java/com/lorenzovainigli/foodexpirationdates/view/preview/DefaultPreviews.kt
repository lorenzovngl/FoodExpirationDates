package com.lorenzovainigli.foodexpirationdates.view.preview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.Navigation
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.SettingsScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.getItemsForPreview

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
    fun MainScreenDynamicColorsPreview() {
        FoodExpirationDatesTheme {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                MainScreen(
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
                SettingsScreen()
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