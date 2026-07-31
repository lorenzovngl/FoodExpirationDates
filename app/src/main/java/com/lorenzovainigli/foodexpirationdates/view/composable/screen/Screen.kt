package com.lorenzovainigli.foodexpirationdates.view.composable.screen

sealed class Screen(
    val route: String,
    val name: String
) {
    data object MainScreen : Screen("main_screen", "Main Screen")
    data object InsertScreen : Screen("insert_screen", "Insert Screen")
    data object AboutScreen : Screen("about_screen", "About Screen")
    data object SettingsScreen : Screen("setting_screen", "Settings Screen")
    data object NewsScreen : Screen("news_screen", "News Screen")

    companion object {
        fun fromRoute(route: String?): Screen? {
            val baseRoute = route?.substringBefore("?")
            return when (baseRoute) {
                MainScreen.route -> MainScreen
                InsertScreen.route -> InsertScreen
                AboutScreen.route -> AboutScreen
                SettingsScreen.route -> SettingsScreen
                NewsScreen.route -> NewsScreen
                else -> null
            }
        }
    }
}
