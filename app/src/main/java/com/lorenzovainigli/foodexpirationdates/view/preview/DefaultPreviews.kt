package com.lorenzovainigli.foodexpirationdates.view.preview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

class DefaultPreviews {
    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
                MainScreen(navController = navController)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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