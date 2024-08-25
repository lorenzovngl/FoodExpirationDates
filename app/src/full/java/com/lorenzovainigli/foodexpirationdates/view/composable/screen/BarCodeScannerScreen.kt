package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.util.Log
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.lorenzovainigli.foodexpirationdates.model.repository.ConnectivityRepository
import com.lorenzovainigli.foodexpirationdates.view.BarcodeScannerActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.BarcodeScannerResult
import com.lorenzovainigli.foodexpirationdates.view.composable.CameraPreview
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel.*

@Composable
fun BarCodeScannerScreen(activity: BarcodeScannerActivity) {
    val context = LocalContext.current
    val apiServiceViewModel: APIServiceViewModel = activity.apiServiceViewModel
    val barcodeScannerState = apiServiceViewModel.barcodeScannerState.collectAsState()
    val barcodeScanner = BarcodeScanning.getClient()
    val previousBarcodeDetected = remember {
        mutableStateOf("")
    }
    val detectedProduct = apiServiceViewModel.product.collectAsState()
    val responseStatus = apiServiceViewModel.responseStatus.collectAsState()
    Log.i("detectedProduct", detectedProduct.value.toString())
    val isOnline = ConnectivityRepository(context).isConnected.collectAsState(initial = false)
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_ANALYSIS
            )
            setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(context),
                MlKitAnalyzer(
                    listOf(barcodeScanner),
                    COORDINATE_SYSTEM_VIEW_REFERENCED,
                    ContextCompat.getMainExecutor(context)
                ) { result: MlKitAnalyzer.Result? ->
                    val barcodeResults = result?.getValue(barcodeScanner)
                    if (!barcodeResults.isNullOrEmpty() && isOnline.value) {
                        for (barcode in barcodeResults) {
                            barcode.displayValue?.let {
                                if (it != previousBarcodeDetected.value) {
                                    apiServiceViewModel.setBarcodeScannerState(
                                        BarcodeScannerState.GETTING_PRODUCT_INFO)
                                    apiServiceViewModel.run(it)
                                    previousBarcodeDetected.value = it
                                    Log.i("detectedProduct", detectedProduct.value.toString())
                                }
                            }
                        }
                    }
                }
            )
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (!isOnline.value) {
            BarcodeScannerResult(
                activity = activity,
                state = BarcodeScannerState.NO_CONNECTION
            )
        } else {
            when (barcodeScannerState.value) {
                BarcodeScannerState.GOT_RESULT ->
                    BarcodeScannerResult(
                        activity = activity,
                        state = BarcodeScannerState.GOT_RESULT,
                        responseOk = responseStatus.value,
                        detectedProduct = detectedProduct.value
                    )

                else ->
                    BarcodeScannerResult(
                        activity = activity,
                        state = barcodeScannerState.value
                    )
            }
        }
    }
}