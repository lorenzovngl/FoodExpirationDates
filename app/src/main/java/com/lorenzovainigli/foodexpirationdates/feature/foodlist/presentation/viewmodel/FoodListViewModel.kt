package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsEvent
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.mapper.FoodCardUiModelMapper
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodListUiState
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: ExpirationDateRepository,
    private val foodCardUiModelMapper: FoodCardUiModelMapper,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodListUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedItem = MutableStateFlow<ExpirationDate?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    private val _deletedItem: MutableState<ExpirationDate?> = mutableStateOf(value = null)
    val deletedItem: State<ExpirationDate?> = _deletedItem

    init {
        observeFoodItems()
    }

    private fun observeFoodItems() {
        viewModelScope.launch {
            repository.getAll()
                .map { expirationDates ->
                    expirationDates
                        .sortedBy(::computeExpirationDate)
                        .map(foodCardUiModelMapper::map)
                        .toImmutableList()
                }.collect { items ->
                    _uiState.update {
                        it.copy(
                            items = items,
                            isLoading = false,
                        )
                    }
                }
        }
    }

    fun loadFoodItem(id: Int) {
        viewModelScope.launch {
            _selectedItem.value = repository.getOne(id)
        }
    }

    fun addFoodItem(expirationDate: ExpirationDate) {
        viewModelScope.launch {
            repository.addExpirationDate(expirationDate)
            analyticsTracker.logEvent(AnalyticsEvent.FOOD_ADDED)
        }
    }

    fun deleteFoodItem(itemId: Int) {
        viewModelScope.launch {
            repository.deleteExpirationDate(itemId)
            _deletedItem.value = repository.getOne(itemId)
            analyticsTracker.logEvent(AnalyticsEvent.FOOD_DELETED)
        }
    }

}