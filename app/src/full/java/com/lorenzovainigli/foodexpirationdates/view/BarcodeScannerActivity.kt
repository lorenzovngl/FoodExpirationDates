package com.lorenzovainigli.foodexpirationdates.view

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.util.PermissionUtils
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.BarCodeScannerScreen
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BarcodeScannerActivity: ComponentActivity() {

    val apiServiceViewModel: APIServiceViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        PermissionUtils.requestPermission(
            activity = this,
            permission = Manifest.permission.CAMERA
        )
//        try {
            setContent {
                val prefsViewModel: PreferencesViewModel = viewModel()
                val prefsUiState by prefsViewModel.uiState.collectAsStateWithLifecycle()
                val isInDarkTheme = when (prefsUiState.themeMode) {
                    PreferencesRepository.ThemeMode.LIGHT -> false
                    PreferencesRepository.ThemeMode.DARK -> true
                    else -> isSystemInDarkTheme()
                }
                FoodExpirationDatesTheme(
                    darkTheme = isInDarkTheme,
                    dynamicColor = prefsUiState.dynamicColorsEnabled
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        BarCodeScannerScreen(
                            activity = this
                        )
                    }
                }
            }
//        } catch (e: Exception){
//            e.printStackTrace()
//            if (BuildConfig.FLAVOR != "foss") {
//                com.google.firebase.crashlytics.FirebaseCrashlytics.getInstance()
//                    .log("Exception: $e")
//            }
//        }
    }

}