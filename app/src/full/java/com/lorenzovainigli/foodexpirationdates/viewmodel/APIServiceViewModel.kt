package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsAPIService
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsJsonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class APIServiceViewModel @Inject constructor(
    private val service: OpenFoodFactsAPIService
) : ViewModel() {

    enum class BarcodeScannerState {
        NO_CONNECTION, READY_TO_SCAN, GETTING_PRODUCT_INFO, SCANNING_ERROR, GOT_RESULT, NETWORK_ERROR
    }

    private val _barcodeScannerState: MutableStateFlow<BarcodeScannerState> = MutableStateFlow(value = BarcodeScannerState.READY_TO_SCAN)
    val barcodeScannerState: StateFlow<BarcodeScannerState> = _barcodeScannerState.asStateFlow()

    private val _responseStatus: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val responseStatus: StateFlow<Boolean> = _responseStatus.asStateFlow()

    private val _product: MutableStateFlow<OpenFoodFactsJsonResponse?> = MutableStateFlow(value = null)
    val product: StateFlow<OpenFoodFactsJsonResponse?> = _product.asStateFlow()

    fun run(barcode: String){
        _barcodeScannerState.value = BarcodeScannerState.GETTING_PRODUCT_INFO
        viewModelScope.launch {
            try {
                val response = service.getProduct(barcode)
                _responseStatus.value = response.isSuccessful
                if (response.isSuccessful) {
                    val item = response.body()
                    if (item != null) {
                        Log.i("Retrofit", item.toString())
                        _product.value = item
                        _barcodeScannerState.value = BarcodeScannerState.GOT_RESULT
                    } else {
                        _barcodeScannerState.value = BarcodeScannerState.SCANNING_ERROR
                    }
                } else {
                    _barcodeScannerState.value = BarcodeScannerState.SCANNING_ERROR
                    Log.e("Retrofit error", response.code().toString())
                }
            } catch (e: Exception) {
                _barcodeScannerState.value = when (e) {
                    is UnknownHostException -> BarcodeScannerState.NO_CONNECTION
                    is SocketTimeoutException, is IOException -> BarcodeScannerState.NETWORK_ERROR
                    else -> BarcodeScannerState.SCANNING_ERROR
                }
                Log.e("APIServiceViewModel", "Error fetching product", e)
            }
        }
    }

    fun setBarcodeScannerState(newState: BarcodeScannerState){
        _barcodeScannerState.value = newState
    }
}