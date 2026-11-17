package com.lorenzovainigli.foodexpirationdates.view.composable.scaffold

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScaffoldTopBar(
    uiState: MyScaffoldUiState,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackClick: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
) {
    val currentDestination = uiState.currentDestination

    val isInsertScreen =
        currentDestination.contains(Screen.InsertScreen.route)

    val isAboutScreen =
        currentDestination.contains(Screen.AboutScreen.route)

    val title = when (currentDestination) {
        Screen.AboutScreen.route ->
            stringResource(R.string.about_this_app)

        Screen.SettingsScreen.route ->
            stringResource(R.string.settings)

        Screen.NewsScreen.route ->
            stringResource(com.lorenzovainigli.news.R.string.news)

        else -> {
            if (isInsertScreen) {
                stringResource(
                    if (uiState.isEditingItem) {
                        R.string.edit_item
                    } else {
                        R.string.add_item
                    }
                )
            } else {
                stringResource(R.string.app_name)
            }
        }
    }

    MyTopAppBar(
        title = title,
        topBarFont = uiState.topBarFont,
        actions = actions,
        navigationIcon = {
            if (isInsertScreen || isAboutScreen) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
        monochromeIcons = uiState.monochromeIconsEnabled,
    )
}
