package com.lorenzovainigli.foodexpirationdates.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lorenzovainigli.foodexpirationdates.analytics.ScreenViewTracker
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.route.SettingsRoute
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.screen.FoodEditorScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route.FoodListRoute
import com.lorenzovainigli.news.presentation.route.NewsRoute

@Composable
fun Navigation(
    activity: MainActivity? = null,
    navController: NavHostController,
    startDestination: String = Screen.MainScreen.route,
    showSnackbar: MutableState<Boolean>,
    isSearchActive: Boolean = false,
    onSearchBarClose: () -> Unit = {}
) {

    ScreenViewTracker(
        navController = navController
    )

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.MainScreen.route) {
            FoodListRoute(
                onAddFoodItem = {
                    navController.navigate(Screen.InsertScreen.route)
                },
                onEditFoodItem = { id ->
                    navController.navigate(Screen.InsertScreen.route + "?itemId=${id}")
                }
            )
        }
        composable(
            route = Screen.InsertScreen.route + "?itemId={itemId}",
            arguments = listOf(
                navArgument("itemId"){
                    type = NavType.StringType
                    nullable = true
                }
            )
        ){ entry ->
            FoodEditorScreen(
                activity = activity,
                navController = navController,
                itemId = entry.arguments?.getString("itemId")
            )
        }
        composable(route = Screen.AboutScreen.route){
            InfoScreen()
        }
        composable(route = Screen.SettingsScreen.route){
            SettingsRoute()
        }
        composable(route = Screen.NewsScreen.route){
            NewsRoute()
        }
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Preview
//@Composable
//fun NavigationPreview(){
//    FoodExpirationDatesTheme {
//        Surface(modifier = Modifier.fillMaxSize()) {
//            Navigation(
//                navController = rememberNavController(),
//                coroutineScope = rememberCoroutineScope(),
//                snackbarHostState = SnackbarHostState()
//            )
//        }
//    }
//}