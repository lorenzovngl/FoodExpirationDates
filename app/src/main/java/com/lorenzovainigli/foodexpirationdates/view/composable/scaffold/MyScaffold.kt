
package com.lorenzovainigli.foodexpirationdates.view.composable.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.lorenzovainigli.foodexpirationdates.navigation.NavigationItem
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffold(
    uiState: MyScaffoldUiState,
    navigationItems: List<NavigationItem>,
    snackbarHostState: SnackbarHostState,
    onNavigate: (String) -> Unit,
    topBar: @Composable (TopAppBarScrollBehavior) -> Unit,
    content: @Composable () -> Unit,
) {
    if (uiState.currentDestination == Screen.OnboardingScreen.route) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            content()
        }
        return
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    NavigationSuiteScaffold(
        navigationItemVerticalArrangement = Arrangement.Center,
        navigationItems = {
            navigationItems.forEach { item ->
                val selected = uiState.currentDestination == item.route

                NavigationSuiteItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            onNavigate(item.route)
                        }
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.showBadge) {
                                    Badge {
                                        item.badgeNumber?.let {
                                            Text(text = it.toString())
                                        }
                                    }
                                }
                            },
                        ) {
                            Icon(
                                imageVector = if (selected) {
                                    item.selectedIcon
                                } else {
                                    item.unselectedIcon
                                },
                                contentDescription = item.label,
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.label,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(
                scrollBehavior.nestedScrollConnection
            ),
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            },
            topBar = {
                topBar(scrollBehavior)
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
            ) {
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
private fun ScaffoldPreviewContent() {
    val uiState = MyScaffoldUiState(
        currentDestination = Screen.MainScreen.route,
        showPermissionBanner = true,
        unreadNewsCount = 5,
    )
    FoodExpirationDatesTheme {
        MyScaffold(
            uiState = uiState,
            navigationItems = scaffoldNavigationItems(uiState),
            snackbarHostState = remember {
                SnackbarHostState()
            },
            onNavigate = {},
            topBar = { scrollBehavior ->
                MyScaffoldTopBar(
                    uiState = MyScaffoldUiState(
                        currentDestination = Screen.MainScreen.route,
                    ),
                    scrollBehavior = scrollBehavior,
                    onBackClick = {},
                )
            },
        ) {
            Text("Content")
        }
    }
}
