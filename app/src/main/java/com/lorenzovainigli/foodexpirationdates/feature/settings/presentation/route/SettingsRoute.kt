package com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.screen.SettingsScreen
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel

@Composable
fun SettingsRoute(
    viewModel: PreferencesViewModel= hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onDateFormatChange = {
            viewModel.setDateFormat(context, it)
        },
        onNotificationTimeChange = { hour, minute ->
            viewModel.setNotificationTime(context, hour, minute)
            NotificationManager.scheduleDailyNotification(
                context, hour, minute
            )
        },
        onThemeModeChange = {
            viewModel.setThemeMode(context, it)
        },
        onDynamicColorsChange = {
            viewModel.setDynamicColors(context, it)
        },
        onTopBarFontChange = {
            viewModel.setTopBarFont(context, it)
        },
        onMonochromeIconsChange = {
            viewModel.setMonochromeIcons(context, it)
        }
    )
}