package com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model

import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.ThemeMode
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.TopBarFont

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val dynamicColorsEnabled: Boolean = false,
    val monochromeIconsEnabled: Boolean = true,
    val topBarFont: TopBarFont = TopBarFont.NORMAL,
    val dateFormat: String = PreferencesRepository.getAvailOtherDateFormats()[0],
    val notificationHour: Int = 11,
    val notificationMinute: Int = 0,
    val screenProtectionEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true
)