package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.Feed
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.util.areNotificationsEnabled
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import com.lorenzovainigli.foodexpirationdates.viewmodel.MyBottomAppBarViewModel

@Composable
fun MyBottomAppBar(
    navController: NavHostController,
    currentDestination: String?
){
    val context = LocalContext.current
    var showPermissionBanner by remember {
        mutableStateOf(!areNotificationsEnabled(context))
    }
    LifecycleResumeEffect(Unit) {
        showPermissionBanner = !areNotificationsEnabled(context)
        onPauseOrDispose { }
    }
    val viewModel: MyBottomAppBarViewModel = hiltViewModel()
    val unreadNewsCount by viewModel.unreadNewsCount.collectAsStateWithLifecycle()
    val navigationItems = listOf(
        NavigationItem(
            label = stringResource(id = R.string.list),
            route = Screen.MainScreen.route,
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List
        ),
        NavigationItem(
            label = stringResource(id = R.string.settings),
            route = Screen.SettingsScreen.route,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            showBadge = showPermissionBanner
        ),
        NavigationItem(
            label = stringResource(id = R.string.about_this_app),
            route = Screen.AboutScreen.route,
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItem(
            label = stringResource(com.lorenzovainigli.news.R.string.news),
            route = Screen.NewsScreen.route,
            selectedIcon = Icons.AutoMirrored.Filled.Feed,
            unselectedIcon = Icons.AutoMirrored.Outlined.Feed,
            showBadge = unreadNewsCount > 0,
            badgeNumber = unreadNewsCount
        )
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(TonalElevation.level2())) {
        var selectedItem = when (currentDestination) {
            Screen.NewsScreen.route -> 3
            Screen.AboutScreen.route -> 2
            Screen.SettingsScreen.route -> 1
            else -> 0
        }
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route)
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.showBadge) {
                                Badge {
                                    item.badgeNumber?.let {
                                        Text("${item.badgeNumber}")
                                    }
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = when (selectedItem == index) {
                                true -> item.selectedIcon
                                else -> item.unselectedIcon
                            },
                            contentDescription = item.label
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@PreviewLightDark
@LanguagePreviews
@Composable
fun MyBottomAppBarPreview(){
    FoodExpirationDatesTheme {
        Surface {
            MyBottomAppBar(
                navController = rememberNavController(),
                currentDestination = Screen.AboutScreen.route
            )
        }
    }
}