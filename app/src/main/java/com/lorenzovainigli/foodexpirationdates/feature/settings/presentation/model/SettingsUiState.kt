package com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model

import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SettingsUiState(
    val themeMode: PreferencesRepository.ThemeMode = PreferencesRepository.ThemeMode.SYSTEM,
    val dynamicColorsEnabled: Boolean = false,
    val monochromeIconsEnabled: Boolean = true,
    val topBarFont: PreferencesRepository.TopBarFont = PreferencesRepository.TopBarFont.NORMAL,
    val dateFormat: String = "d MMM",
    val availLocaleDateFormats: ImmutableList<String> = emptyList<String>().toImmutableList(),
    val availOtherDateFormats: ImmutableList<String> = emptyList<String>().toImmutableList(),
    val notificationHour: Int = 11,
    val notificationMinute: Int = 0,
    val screenProtectionEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val language: Language = Language.SYSTEM
)