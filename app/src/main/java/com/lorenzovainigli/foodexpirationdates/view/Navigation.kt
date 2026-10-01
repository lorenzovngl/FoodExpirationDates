package com.lorenzovainigli.foodexpirationdates.view

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.lorenzovainigli.foodexpirationdates.PLAY_STORE_URL
import com.lorenzovainigli.foodexpirationdates.WEBSITE_URL_EN
import com.lorenzovainigli.foodexpirationdates.WEBSITE_URL_IT
import com.lorenzovainigli.foodexpirationdates.analytics.ScreenViewTracker
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.route.SettingsRoute
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.screen.FoodEditorScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.route.FoodListRoute
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.feature.info.presentation.route.InfoRoute
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewManager
import com.lorenzovainigli.news.presentation.route.NewsRoute
import java.util.Locale
import kotlin.Boolean

@Composable
fun Navigation(
    activity: MainActivity,
    showSnackbar: MutableState<Boolean>?,
    isSearchActive: Boolean = false,
    navController: NavHostController,
    startDestination: String = Screen.MainScreen.route,
    reviewManager: ReviewManager,
    onSearchBarClose: () -> Unit
) {

    ScreenViewTracker(
        navController = navController
    )

    val foodListViewModel: FoodListViewModel = hiltViewModel()
    LaunchedEffect(foodListViewModel) {
        foodListViewModel.requestReview.collect {
            activity?.let {
                reviewManager.requestReview(
                    activity = it,
                    isAutomatic = false,
                )
            }
        }
    }

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.MainScreen.route) {
            FoodListRoute(
                viewModel = foodListViewModel,
                showSnackbar = showSnackbar,
                isSearchActive = isSearchActive,
                onAddFoodItem = {
                    navController.navigate(Screen.InsertScreen.route)
                },
                onEditFoodItem = { id ->
                    navController.navigate(Screen.InsertScreen.route + "?itemId=$id")
                },
                onSearchBarClose = onSearchBarClose
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
            val itemToEditId = entry.arguments?.getString("itemId")
            FoodEditorScreen(
                itemToEdit = itemToEditId?.let {
                    activity?.viewModel?.getExpirationDate(it.toInt())
                },
                onSave = { entry ->
                    activity?.foodListViewModel?.addFoodItem(entry)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
        composable(route = Screen.AboutScreen.route){
            val uriHandler = LocalUriHandler.current
            InfoRoute(
                onClickShare = {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            if (Locale.getDefault().language == "it") WEBSITE_URL_IT
                            else WEBSITE_URL_EN
                        )
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    activity?.startActivity(shareIntent)
                },
                onClickReview = {
                    if (activity != null) {
                        reviewManager.requestReview(activity)
                    } else {
                        uriHandler.openUri(
                            uri = PLAY_STORE_URL
                        )
                    }
                },
            )
        }
        composable(route = Screen.SettingsScreen.route){
            SettingsRoute(activity.preferencesViewModel)
        }
        composable(route = Screen.NewsScreen.route){
            NewsRoute()
        }
    }
}
