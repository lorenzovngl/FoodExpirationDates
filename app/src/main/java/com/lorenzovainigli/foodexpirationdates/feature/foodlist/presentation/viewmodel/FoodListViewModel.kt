package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsEvent
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport.ExpirationDateImportExportManager
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.event.FoodDeletedEvent
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.mapper.FoodCardUiModelMapper
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodListUiState
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewRequest
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewRequestStrategy
import com.lorenzovainigli.foodexpirationdates.util.OperationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodListViewModel @Inject constructor(
    private val repository: ExpirationDateRepository,
    private val preferencesRepository: PreferencesRepository,
    private val foodCardUiModelMapper: FoodCardUiModelMapper,
    private val analyticsTracker: AnalyticsTracker,
    private val reviewRequestStrategy: ReviewRequestStrategy,
    private val importExportManager: ExpirationDateImportExportManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodListUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedItem = MutableStateFlow<ExpirationDate?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    private val _requestReview = MutableSharedFlow<ReviewRequest>()
    val requestReview = _requestReview.asSharedFlow()

    private val _exportTaskSuccess = MutableStateFlow(true)
    val exportTaskSuccess = _exportTaskSuccess.asStateFlow()

    private val _notifyExportTaskDone = MutableStateFlow(false)
    val notifyExportTaskDone = _notifyExportTaskDone.asStateFlow()

    private val _importResult = MutableStateFlow<OperationResult?>(null)
    val importResult = _importResult.asStateFlow()

    private val _foodDeletedEvents = Channel<FoodDeletedEvent>(Channel.BUFFERED)
    val foodDeletedEvents: Flow<FoodDeletedEvent> = _foodDeletedEvents.receiveAsFlow()

    init {
        observeFoodItems()
    }

    private fun observeFoodItems() {
        viewModelScope.launch {
            combine(
                repository.getAll(),
                preferencesRepository.settingsFlow
                    .map { it.dateFormat }
                    .distinctUntilChanged(),
            ) { expirationDates, dateFormat ->
                expirationDates
                    .sortedBy(::computeExpirationDate)
                    .map { item ->
                        foodCardUiModelMapper.map(
                            item = item,
                            dateFormat = dateFormat,
                        )
                    }
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

            val result = reviewRequestStrategy.onFoodAdded()

            if (result.shouldRequestReview) {
                _requestReview.emit(
                    ReviewRequest(
                        foodCount = result.foodCount,
                        isAutomatic = false,
                    )
                )
            }
        }
    }

    fun deleteFoodItem(itemId: Int) {
        viewModelScope.launch {
            val item = repository.getOne(itemId)
            repository.deleteExpirationDate(itemId)
            analyticsTracker.logEvent(AnalyticsEvent.FOOD_DELETED)
            _foodDeletedEvents.send(
                FoodDeletedEvent(item = item)
            )
        }
    }

    fun deleteAllFoodItems() {
        viewModelScope.launch {
            repository.deleteAll()
            analyticsTracker.logEvent(AnalyticsEvent.FOOD_DELETED)
        }
    }

    fun undoDelete(item: ExpirationDate) {
        viewModelScope.launch {
            repository.addExpirationDate(item)
        }
    }

    fun exportData() {
        viewModelScope.launch {
            val result = importExportManager.export()

            _exportTaskSuccess.value = result.state == OperationResult.State.SUCCESS

            _notifyExportTaskDone.value = true
        }
    }

    fun resetNotifyExportTaskDone() {
        _notifyExportTaskDone.value = false
    }

    fun importData(
        contentResolver: ContentResolver,
        uri: Uri?,
    ) {
        viewModelScope.launch {
            _importResult.value = importExportManager.import(
                contentResolver = contentResolver,
                uri = uri,
            )
        }
    }

    fun resetImportResult() {
        _importResult.value = null
    }

}