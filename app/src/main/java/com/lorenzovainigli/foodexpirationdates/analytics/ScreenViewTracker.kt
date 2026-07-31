package com.lorenzovainigli.foodexpirationdates.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@Composable
fun ScreenViewTracker(
    navController: NavHostController
) {
    val analyticsTracker = LocalAnalyticsTracker.current
    val backStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(backStackEntry) {
        val route = backStackEntry?.destination?.route
        val screen = Screen.fromRoute(route)

        screen?.let {
            analyticsTracker.logScreenView(it)
        }
    }
}