package com.lorenzovainigli.foodexpirationdates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlinx.coroutines.delay

@HiltViewModel
class ExpirationDatesViewModel @Inject constructor(
    private val repository: ExpirationDateRepository
    ) : ViewModel() {

    private var expirationDates : Flow<List<ExpirationDate>> = flowOf(emptyList())
    private var expirationDate : ExpirationDate? = null

    private val _isSplashScreenLoading: MutableState<Boolean> = mutableStateOf(value = true)
    val isSplashScreenLoading: State<Boolean> = _isSplashScreenLoading

    fun getDates(): Flow<List<ExpirationDate>> {
        viewModelScope.launch {
            _isSplashScreenLoading.value = true
            expirationDates = repository.getAll()
            delay(2000)
            _isSplashScreenLoading.value = false
        }
        return expirationDates
    }

    fun getDate(id: Int): ExpirationDate? {
        viewModelScope.launch {
            expirationDate = repository.getOne(id)
        }
        return expirationDate
    }

    fun addExpirationDate(expirationDate: ExpirationDate) {
        viewModelScope.launch {
            repository.addExpirationDate(expirationDate)
            expirationDates = repository.getAll()
        }
    }

    fun deleteExpirationDate(expirationDate: ExpirationDate) {
        viewModelScope.launch {
            repository.deleteExpirationDate(expirationDate)
            expirationDates = repository.getAll()
        }
    }
}
