package com.lorenzovainigli.foodexpirationdates.view.composable.scaffold

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.util.areNotificationsEnabled
import com.lorenzovainigli.foodexpirationdates.view.composable.MainScreenMenu
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.viewmodel.MyBottomAppBarViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldRoute(
    navController: NavHostController,
    startDestination: String = Screen.MainScreen.route,
    onSearchIconClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val viewModelStoreOwner = LocalActivity.current as ComponentActivity

    val preferencesViewModel: PreferencesViewModel = hiltViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
    )
    val foodListViewModel: FoodListViewModel = hiltViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
    )
    val viewModel: MyBottomAppBarViewModel = hiltViewModel()

    val snackbarHostState = remember { SnackbarHostState() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = currentBackStackEntry?.destination?.route ?: startDestination

    val undoLabel = stringResource(R.string.undo)
    val deletedMessageTemplate = stringResource(R.string.x_deleted, "%s")
    LaunchedEffect(foodListViewModel, undoLabel, deletedMessageTemplate) {
        foodListViewModel.foodDeletedEvents.collect { event ->
            val result = snackbarHostState.showSnackbar(
                message = deletedMessageTemplate.replace("%s", event.item.foodName),
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )

            if (result == SnackbarResult.ActionPerformed) {
                foodListViewModel.undoDelete(event.item)
            }
        }
    }
    var showPermissionBanner by remember {
        mutableStateOf(!areNotificationsEnabled(context))
    }
    LifecycleResumeEffect(Unit) {
        showPermissionBanner = !areNotificationsEnabled(context)
        onPauseOrDispose { }
    }
    val unreadNewsCount by viewModel.unreadNewsCount.collectAsStateWithLifecycle()
    val prefsUiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()
    val uiState = MyScaffoldUiState(
        currentDestination = currentDestination,
        isEditingItem = currentBackStackEntry
            ?.arguments
            ?.getString("itemId") != null,
        showPermissionBanner = showPermissionBanner,
        unreadNewsCount = unreadNewsCount,
        topBarFont = prefsUiState.topBarFont.ordinal,
        monochromeIconsEnabled = prefsUiState.monochromeIconsEnabled,
    )
    MyScaffold(
        uiState = uiState,
        navigationItems = scaffoldNavigationItems(uiState),
        snackbarHostState = snackbarHostState,
        onNavigate = { route ->
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(Screen.MainScreen.route) {
                    saveState = true
                }
            }
        },
        topBar = { scrollBehavior ->
            MyScaffoldTopBar(
                uiState = uiState,
                scrollBehavior = scrollBehavior,
                onBackClick = {
                    navController.popBackStack()
                },
                actions = {
                    if (currentDestination.contains(Screen.MainScreen.route)) {
                        val context = LocalContext.current
                        val viewModel = foodListViewModel
                        val exportTaskSuccess = viewModel.exportTaskSuccess.collectAsStateWithLifecycle()
                        val notifyExportTaskDone = viewModel.notifyExportTaskDone.collectAsStateWithLifecycle()

                        val importFileLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.OpenDocument()
                        ) { uri ->
                            if (uri != null) {
                                viewModel.importData(
                                    contentResolver = context.contentResolver,
                                    uri = uri
                                )
                            }
                        }
                        MainScreenMenu(
                            exportTaskSuccess = exportTaskSuccess,
                            notifyExportTaskDone = notifyExportTaskDone,
                            onSearchClick = onSearchIconClick,
                            onExportClick = {
                                foodListViewModel.exportData()
                            },
                            onImportClick = {
                                importFileLauncher.launch(arrayOf("*/*"))
                            },
                            onClearAllClick = {
                                foodListViewModel.deleteAllFoodItems()
                            },
                            onInfoClick = {
                                navController.navigate(Screen.AboutScreen.route)
                            },
                            onExportErrorDialogDismiss = {
                                foodListViewModel.resetNotifyExportTaskDone()
                            }
                        )
                    } else if (!currentDestination.contains(Screen.AboutScreen.route) && !currentDestination.contains(
                            Screen.InsertScreen.route
                        )
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(Screen.AboutScreen.route)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = stringResource(R.string.about_this_app),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
            )
        },
        content = content
    )
}
