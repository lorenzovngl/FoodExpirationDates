package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsAPIService
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsJsonResponse
import javax.inject.Inject

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@HiltViewModel
class APIServiceViewModel @Inject constructor() : ViewModel() {

    private val BASE_URL = "https://de.openfoodfacts.org/api/v2/product/"

    enum class BarcodeScannerState {
        NO_CONNECTION, READY_TO_SCAN, GETTING_PRODUCT_INFO, SCANNING_ERROR, GOT_RESULT
    }

    private val _barcodeScannerState: MutableStateFlow<BarcodeScannerState> = MutableStateFlow(value = BarcodeScannerState.READY_TO_SCAN)
    val barcodeScannerState: StateFlow<BarcodeScannerState> = _barcodeScannerState.asStateFlow()

    private val _responseStatus: MutableStateFlow<Boolean> = MutableStateFlow(value = false)
    val responseStatus: StateFlow<Boolean> = _responseStatus.asStateFlow()

    private val _product: MutableStateFlow<OpenFoodFactsJsonResponse?> = MutableStateFlow(value = null)
    val product: StateFlow<OpenFoodFactsJsonResponse?> = _product.asStateFlow()

    private val instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun run(barcode: String){
        val service = instance.create(OpenFoodFactsAPIService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getProduct(barcode)
            withContext(Dispatchers.Main){
                _responseStatus.value = response.isSuccessful
                if (response.isSuccessful){
                    _barcodeScannerState.value = BarcodeScannerState.GOT_RESULT
                    val item = response.body()
                    if (item != null){
                        Log.i("Retrofit", item.toString())
                        _product.value = item
                    }
                } else {
                    _barcodeScannerState.value = BarcodeScannerState.SCANNING_ERROR
                    Log.e("Retrofit error", response.code().toString())
                }
            }
        }
    }

    fun setBarcodeScannerState(newState: BarcodeScannerState){
        _barcodeScannerState.value = newState
    }
}