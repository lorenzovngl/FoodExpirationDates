package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model.SettingsUiState
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private var _themeMode = MutableStateFlow(0)
    private var themeMode = _themeMode.asStateFlow()
    private var _dynamicColors = MutableStateFlow(false)
    private var dynamicColors = _dynamicColors.asStateFlow()
    private var _topBarFont = MutableStateFlow(0)
    private var topbarFont = _topBarFont.asStateFlow()

    init {
        viewModelScope.launch {
            val dateFormat = PreferencesRepository.getUserDateFormat(context)
            val notificationHour = PreferencesRepository.getUserNotificationTimeHour(context)
            val notificationMinute = PreferencesRepository.getUserNotificationTimeMinute(context)

            _uiState.update {
                it.copy(
                    dateFormat = dateFormat,
                    notificationHour = notificationHour,
                    notificationMinute = notificationMinute
                )
            }
        }
    }

    fun setDateFormat(context: Context, format: String) {
        viewModelScope.launch {
            PreferencesRepository.setUserDateFormat(
                context = context,
                dateFormat = format
            )
            _uiState.update {
                it.copy(
                    dateFormat = format
                )
            }
        }
    }

    fun setNotificationTime(context: Context, hour: Int, minute: Int) {
        viewModelScope.launch {
            PreferencesRepository.setUserNotificationTime(
                context = context,
                hour = hour,
                minute = minute
            )
            _uiState.value = _uiState.value.copy(
                notificationHour = hour,
                notificationMinute = minute
            )
        }
    }

    fun getThemeMode(context: Context): StateFlow<Int> {
        viewModelScope.launch {
            _themeMode.value = PreferencesRepository.getThemeMode(context)
        }
        return themeMode
    }
    fun setThemeMode(context: Context, theme: PreferencesRepository.Companion.ThemeMode) {
        viewModelScope.launch {
            PreferencesRepository.setThemeMode(
                context = context,
                themeMode = theme
            )
        }
        _themeMode.value = theme.ordinal
    }

    fun getTopBarFont(context: Context):StateFlow<Int> {
        viewModelScope.launch {
            _topBarFont.value  = PreferencesRepository.getTopBarFont(context)
        }
        return topbarFont
    }

    fun setTopBarFont(context: Context, topBarFont: PreferencesRepository.Companion.TopBarFont) {
        viewModelScope.launch {
            PreferencesRepository.setTopBarFont(
                context = context,
                topBarFont = topBarFont
            )
        }
        _topBarFont.value = topBarFont.ordinal
    }

    fun getDynamicColors(context: Context): StateFlow<Boolean> {
        viewModelScope.launch {
            _dynamicColors.value = PreferencesRepository.getDynamicColors(context)
        }
        return dynamicColors
    }

    fun setDynamicColors(context: Context, colors: Boolean) {
        viewModelScope.launch {
            PreferencesRepository.setDynamicColors(
                context = context,
                dynamicColorsEnabled = colors
            )
        }
        _dynamicColors.value = colors
    }

    fun setMonochromeIcons(context: Context, icons: Boolean) {
        viewModelScope.launch {
            PreferencesRepository.setMonochromeIcons(
                context = context,
                monochromeIconsEnabled = icons
            )
            _uiState.value = _uiState.value.copy(
                monochromeIconsEnabled = icons
            )
        }
    }
}
