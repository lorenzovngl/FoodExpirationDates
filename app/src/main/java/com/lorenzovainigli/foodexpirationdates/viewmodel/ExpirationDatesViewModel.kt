package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsEvent
import com.lorenzovainigli.foodexpirationdates.model.entity.CSV_HEADER
import com.lorenzovainigli.foodexpirationdates.model.entity.EXPIRATION_DATE
import com.lorenzovainigli.foodexpirationdates.model.entity.EXPIRATION_DATE_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.FOOD_NAME
import com.lorenzovainigli.foodexpirationdates.model.entity.FOOD_NAME_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.OPENING_DATE
import com.lorenzovainigli.foodexpirationdates.model.entity.OPENING_DATE_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.QUANTITY_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.TIME_SPAN_DAYS
import com.lorenzovainigli.foodexpirationdates.model.entity.TIME_SPAN_DAYS_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.toCSV
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.saveFileToExternalStorage
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.util.OperationResult
import com.lorenzovainigli.news.data.worker.NewsWorkScheduler
import com.opencsv.CSVReader
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpirationDatesViewModel @Inject constructor(
    private val repository: ExpirationDateRepository,
    private val newsWorkScheduler: NewsWorkScheduler,
    private val analyticsTracker: AnalyticsTracker,
    private val preferencesRepository: PreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private var expirationDates: Flow<List<ExpirationDate>> = flowOf(emptyList())
    private var expirationDate: ExpirationDate? = null

    private val _isSplashScreenLoading: MutableState<Boolean> = mutableStateOf(value = true)
    val isSplashScreenLoading: State<Boolean> = _isSplashScreenLoading

    private val _requestReview = MutableSharedFlow<Unit>()
    val requestReview = _requestReview.asSharedFlow()

    init {
        viewModelScope.launch {
            newsWorkScheduler.scheduleRefreshIfNeeded()
        }
    }

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
            analyticsTracker.logEvent(AnalyticsEvent.FOOD_ADDED)

            val count = preferencesRepository.incrementFoodAddedCount()

            if (count % 50 == 0) {
                _requestReview.emit(Unit)
            }
        }
    }

}
