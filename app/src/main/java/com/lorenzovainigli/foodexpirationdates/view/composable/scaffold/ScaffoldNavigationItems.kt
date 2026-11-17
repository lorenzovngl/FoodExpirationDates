package com.lorenzovainigli.foodexpirationdates.view.composable.scaffold

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.navigation.NavigationItem
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews

@Composable
fun scaffoldNavigationItems(
    uiState: MyScaffoldUiState,
): List<NavigationItem> = listOf(
    NavigationItem(
        label = stringResource(R.string.list),
        route = Screen.MainScreen.route,
        selectedIcon = Icons.AutoMirrored.Filled.List,
        unselectedIcon = Icons.AutoMirrored.Outlined.List,
    ),
    NavigationItem(
        label = stringResource(R.string.settings),
        route = Screen.SettingsScreen.route,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        showBadge = uiState.showPermissionBanner,
    ),
    NavigationItem(
        label = stringResource(com.lorenzovainigli.news.R.string.news),
        route = Screen.NewsScreen.route,
        selectedIcon = Icons.AutoMirrored.Filled.Feed,
        unselectedIcon = Icons.AutoMirrored.Outlined.Feed,
        showBadge = uiState.unreadNewsCount > 0,
        badgeNumber = uiState.unreadNewsCount,
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@LanguagePreviews
@Composable
private fun ScaffoldNavigationItemsPreview() {
    val uiState = MyScaffoldUiState(
        currentDestination = Screen.MainScreen.route,
        showPermissionBanner = true,
        unreadNewsCount = 5,
    )

    FoodExpirationDatesTheme {
        MyScaffold(
            uiState = uiState,
            navigationItems = scaffoldNavigationItems(uiState),
            snackbarHostState = remember { SnackbarHostState() },
            onNavigate = {},
            topBar = { scrollBehavior ->
                MyScaffoldTopBar(
                    uiState = uiState,
                    scrollBehavior = scrollBehavior,
                    onBackClick = {},
                )
            },
            content = {},
        )
    }
}
