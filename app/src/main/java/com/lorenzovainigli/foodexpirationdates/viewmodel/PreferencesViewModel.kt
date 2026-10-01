package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model.SettingsUiState
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        preferencesRepository.settingsFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState()
            )

    fun setDateFormat(format: String) {
        preferencesRepository.setUserDateFormat(
            dateFormat = format
        )
    }

    fun setNotificationTime(hour: Int, minute: Int) {
        preferencesRepository.setUserNotificationTime(
            hour = hour,
            minute = minute
        )
    }

    fun setThemeMode(theme: PreferencesRepository.ThemeMode) {
        preferencesRepository.setThemeMode(
            themeMode = theme
        )
    }

    fun setTopBarFont(topBarFont: PreferencesRepository.TopBarFont) {
        preferencesRepository.setTopBarFont(
            topBarFont = topBarFont
        )
    }

    fun setDynamicColors(enabled: Boolean) {
        preferencesRepository.setDynamicColors(
            enabled = enabled
        )
    }

    fun setMonochromeIcons(enabled: Boolean) {
        preferencesRepository.setMonochromeIcons(
            enabled = enabled
        )
    }

    fun setScreenProtectionEnabled(enabled: Boolean) {
        preferencesRepository.setScreenProtectionEnabled(
            enabled = enabled
        )
    }

    fun setLanguage(language: Language) {
        preferencesRepository.setLanguage(
            language = language.code
        )
        LocaleHelper.changeLanguage(context, language.code)
    }
}
