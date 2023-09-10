package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(): ViewModel() {

    private var _dateFormat = MutableStateFlow("")
    private var dateFormat = _dateFormat.asStateFlow()

    private var _notificationTimeHour = MutableStateFlow(0)
    private var notificationTimeHour = _notificationTimeHour.asStateFlow()

    private var _notificationTimeMinute = MutableStateFlow(0)
    private var notificationTimeMinute = _notificationTimeMinute.asStateFlow()

    private var _themeMode = MutableStateFlow(0)
    private var themeMode = _themeMode.asStateFlow()

    private var _dynamicColors = MutableStateFlow(false)
    private var dynamicColors = _dynamicColors.asStateFlow()

    fun getDateFormat(context: Context): StateFlow<String> {
        viewModelScope.launch {
            _dateFormat.value = PreferencesRepository.getUserDateFormat(context)
        }
        return dateFormat
    }

    fun setDateFormat(context: Context, format: String) {
        viewModelScope.launch {
            PreferencesRepository.setUserDateFormat(
                context = context,
                dateFormat = format
            )
        }
        _dateFormat.value = format
    }

    fun getNotificationTimeHour(context: Context): StateFlow<Int> {
        viewModelScope.launch {
            _notificationTimeHour.value = PreferencesRepository.getUserNotificationTimeHour(context)
        }
        return notificationTimeHour
    }

    fun getNotificationTimeMinute(context: Context): StateFlow<Int> {
        viewModelScope.launch {
            _notificationTimeMinute.value = PreferencesRepository.getUserNotificationTimeMinute(context)
        }
        return notificationTimeMinute
    }

    fun setNotificationTime(context: Context, hour: Int, minute: Int) {
        viewModelScope.launch {
            PreferencesRepository.setUserNotificationTime(
                context = context,
                hour = hour,
                minute = minute
            )
        }
        _notificationTimeHour.value = hour
        _notificationTimeMinute.value = minute
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

}
