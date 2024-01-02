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
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

@HiltViewModel
class ExpirationDatesViewModel @Inject constructor(
    private val repository: ExpirationDateRepository
    ) : ViewModel() {

    private var expirationDates : Flow<List<ExpirationDate>> = flowOf(emptyList())
    private var expirationDate : ExpirationDate? = null

    private val _isSplashScreenLoading: MutableState<Boolean> = mutableStateOf(value = true)
    val isSplashScreenLoading: State<Boolean> = _isSplashScreenLoading

    private val _deletedItem: MutableState<ExpirationDate?> = mutableStateOf(value = null)
    val deletedItem: State<ExpirationDate?> = _deletedItem

    fun getDates(): Flow<List<ExpirationDate>> {
        viewModelScope.launch {
            _isSplashScreenLoading.value = true
            val deferred = async {
                expirationDates = repository.getAll()
                expirationDates = expirationDates.transform { list ->
                    val sortedList = list.sortedWith { item1, item2 ->
                        val expiration1 = computeExpirationDate(item1)
                        val expiration2 = computeExpirationDate(item2)
                        expiration1.compareTo(expiration2)
                    }
                    emit(sortedList)
                }
            }
            delay(1000)
            deferred.await()
            _isSplashScreenLoading.value = false
        }
        return expirationDates
    }

    fun getExpirationDate(id: Int): ExpirationDate? {
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
        _deletedItem.value = expirationDate
    }
}
