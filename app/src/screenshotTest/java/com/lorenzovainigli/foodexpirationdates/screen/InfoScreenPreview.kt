package com.lorenzovainigli.foodexpirationdates.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyBottomAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun InfoScreenPreview() {
    FoodExpirationDatesTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Scaffold(
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.about_this_app),
                        navigationIcon = {},
                        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                        actions = {
                            val isPreview = LocalInspectionMode.current
                            IconButton (
                                modifier = Modifier.focusProperties {
                                    canFocus = !isPreview
                                },
                                onClick = { }
                            ){
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = stringResource(R.string.about_this_app),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        monochromeIcons = false
                    )
                },
                bottomBar = {
                    MyBottomAppBar(
                        currentDestination = Screen.AboutScreen.route,
                        onNavigationItemClick = {},
                        unreadNewsCount = 1,
                        showPermissionBanner = false
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    InfoScreen(
                        onClickShare = {},
                        versionText = stringResource(R.string.version_x, "2.11")
                    )
                }
            }
        }
    }
}