package com.lorenzovainigli.foodexpirationdates.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.SettingsScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.getItemsForPreview
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
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.MainScreen.route) {
            val itemsState = activity?.viewModel?.getDates()?.collectAsState(emptyList())
            val items = itemsState?.value ?: getItemsForPreview(LocalContext.current)
            MainScreen(
                items = items,
                showSnackbar = showSnackbar,
                isSearchActive = isSearchActive,
                onSearchBarClose = onSearchBarClose,
                onClickDelete = { item ->
                    activity?.viewModel?.deleteExpirationDate(item)
                },
                onClickEdit = { item ->
                    navController.navigate(Screen.InsertScreen.route + "?itemId=${item.id}")
                },
                onFloatingActionButtonClick = {
                    navController.navigate(Screen.InsertScreen.route)
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
            InsertScreen(
                activity = activity,
                navController = navController,
                itemId = entry.arguments?.getString("itemId")
            )
        }
        composable(route = Screen.AboutScreen.route){
            InfoScreen()
        }
        composable(route = Screen.SettingsScreen.route){
            SettingsScreen(activity = activity)
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