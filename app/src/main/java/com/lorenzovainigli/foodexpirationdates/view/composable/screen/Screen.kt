package com.lorenzovainigli.foodexpirationdates.view.composable.screen

sealed class Screen(val route: String) {
    data object MainScreen : Screen("main_screen")
    data object InsertScreen : Screen("insert_screen")
    data object AboutScreen : Screen("about_screen")
    data object SettingsScreen : Screen("setting_screen")
}
