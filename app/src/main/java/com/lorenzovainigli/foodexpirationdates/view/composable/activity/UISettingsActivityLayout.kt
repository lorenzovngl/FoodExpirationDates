package com.lorenzovainigli.foodexpirationdates.view.composable.activity

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.SettingsItem
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UISettingsActivityLayout(
    context: Context = LocalContext.current,
    prefsViewModel: PreferencesViewModel? = viewModel()
) {
    val activity = (context as? Activity)
    val topBarFontState = prefsViewModel?.getTopBarFont(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal
    val darkThemeState = prefsViewModel?.getThemeMode(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.ThemeMode.SYSTEM
    val dynamicColorsState = prefsViewModel?.getDynamicColors(context)?.collectAsState()?.value
        ?: false

    val isInDarkTheme = when (darkThemeState) {
        PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
        PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
        else -> isSystemInDarkTheme()
    }

    FoodExpirationDatesTheme(
        darkTheme = isInDarkTheme,
        dynamicColor = dynamicColorsState
    ){
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MyTopAppBar(
                    title = stringResource(id = R.string.ui_settings),
                    navigationIcon = {
                        IconButton(onClick = { activity?.finish() }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = stringResource(id = R.string.back),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SettingsItem(
                    label = stringResource(R.string.top_bar_font_weight)
                ) {
                    PreferencesRepository.Companion.TopBarFont.values().forEach { topBarFont->
                        Spacer(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(0.1f)
                        )
                        if (topBarFont.ordinal == topBarFontState) {
                            Button(onClick = {}) {
                                Text(
                                    text = context.getString(topBarFont.label)
                                )
                            }
                        }
                        if (topBarFont.ordinal != topBarFontState) {
                            OutlinedButton(
                                onClick = {
                                    prefsViewModel?.setTopBarFont(context, topBarFont)
                                },
                            ) {
                                Text(
                                    text = context.getString(topBarFont.label)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@DefaultPreviews
@LanguagePreviews
@Composable
fun UISettingsActivityLayoutPreview() {
    UISettingsActivityLayout()
}