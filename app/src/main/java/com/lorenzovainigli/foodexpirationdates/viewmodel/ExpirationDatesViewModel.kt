package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.model.entity.CSV_HEADER
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.toCSV
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.saveFileToExternalStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExpirationDatesViewModel @Inject constructor(
    private val repository: ExpirationDateRepository
) : ViewModel() {

    private var expirationDates: Flow<List<ExpirationDate>> = flowOf(emptyList())
    private var expirationDate: ExpirationDate? = null

    private val _isSplashScreenLoading: MutableState<Boolean> = mutableStateOf(value = true)
    val isSplashScreenLoading: State<Boolean> = _isSplashScreenLoading

    private val _deletedItem: MutableState<ExpirationDate?> = mutableStateOf(value = null)
    val deletedItem: State<ExpirationDate?> = _deletedItem

    private val _exportTaskSuccess: MutableState<Boolean> = mutableStateOf(value = true)
    val exportTaskSuccess: State<Boolean> = _exportTaskSuccess

    private val _notifyExportTaskDone: MutableState<Boolean> = mutableStateOf(value = false)
    val notifyExportTaskDone: State<Boolean> = _notifyExportTaskDone

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

    fun exportData(context: Context) {
        viewModelScope.launch {
            try {
                val timeStamp: String =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val fileName = "fed_data_$timeStamp.csv"
                val file = File(context.filesDir, fileName)
                val fileWriter = FileWriter(file)
                fileWriter.appendLine(CSV_HEADER)
                for (entry in expirationDates.first()) {
                    fileWriter.appendLine(entry.toCSV())
                }
                fileWriter.flush()
                fileWriter.close()
                saveFileToExternalStorage(context, "file://${file.path}", file.name)
                _exportTaskSuccess.value = true
            } catch (e: IOException) {
                e.printStackTrace()
                _exportTaskSuccess.value = false
            }
            _notifyExportTaskDone.value = true
        }
    }

    fun resetNotifyExportTaskDone(){
        _notifyExportTaskDone.value = false
    }

}
