package com.lorenzovainigli.foodexpirationdates.view.composable

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import kotlinx.coroutines.launch

data class NavigationItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    activity: MainActivity? = null,
    navController: NavHostController,
    navDestination: String? = null,
    showSnackbar: MutableState<Boolean>,
    searchQuery : MutableState<String> = mutableStateOf(""),
    content: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val context = LocalContext.current

    val noBottomBarRoutes = listOf(Screen.InsertScreen.route + "?itemId={itemId}")

    if (showSnackbar.value){
        coroutineScope.launch {
            try {
                val deletedItem = activity?.viewModel?.deletedItem?.value
                val snackbarResult =
                    snackbarHostState.showSnackbar(
                        message = context.resources.getString(
                            R.string.x_deleted,
                            deletedItem?.foodName ?: ""
                        ),
                        actionLabel = context.resources.getString(R.string.undo),
                        duration = SnackbarDuration.Short
                    )
                when (snackbarResult) {
                    SnackbarResult.ActionPerformed -> {
                        deletedItem?.let {
                            activity.viewModel.addExpirationDate(it)
                        }
                    }

                    else -> {
                        Log.i("ERROR", "Error showing snackbar")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        showSnackbar.value = false
    }
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val destination = navDestination ?: currentBackStackEntry?.destination?.route
            MyTopAppBar(
                activity = activity,
                title = when (destination) {
                    Screen.AboutScreen.route -> stringResource(id = R.string.about_this_app)
                    Screen.SettingsScreen.route -> stringResource(id = R.string.settings)
                    else -> {
                        if (destination?.contains(Screen.InsertScreen.route) == true) {
                            if (currentBackStackEntry?.arguments?.getString("itemId") != null){
                                stringResource(id = R.string.edit_item)
                            } else {
                                stringResource(id = R.string.add_item)
                            }
                        } else {
                            stringResource(id = R.string.app_name)
                        }
                    }
                },
                actions = {
                    if (destination?.contains(Screen.MainScreen.route) == true) {
                        MainScreenMenu(activity, searchQuery)
                    }
                },
                navigationIcon = {
                    if (destination?.contains(Screen.InsertScreen.route) == true) {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            val destination = navDestination ?: currentBackStackEntry?.destination?.route
            if (destination !in noBottomBarRoutes) {
                MyBottomAppBar(
                    navController = navController,
                    currentDestination = navDestination ?: currentBackStackEntry?.destination?.route
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            content()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewLightDark
@PreviewScreenSizes
@Composable
fun MyScaffoldPreview() {
    FoodExpirationDatesTheme {
        Surface {
            val navController =  rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(
                navController = navController,
                showSnackbar = showSnackbar,
            ) {

                MainScreen(
                    navController = rememberNavController(),
                )
            }
        }
    }
}