package com.lorenzovainigli.foodexpirationdates.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpirationDateViewModel @Inject constructor(
    private val repository: ExpirationDateRepository
    ) : ViewModel() {

    private var expirationDates : Flow<List<ExpirationDate>> = flowOf(emptyList())
    private var expirationDate : ExpirationDate? = null

    fun getDates(): Flow<List<ExpirationDate>> {
        viewModelScope.launch {
            expirationDates = repository.getAll()
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
