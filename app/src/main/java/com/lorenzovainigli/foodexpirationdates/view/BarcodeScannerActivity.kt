package com.lorenzovainigli.foodexpirationdates.view

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
        PermissionUtils.requestPermission(
            activity = this,
            permission = Manifest.permission.CAMERA
        )
//        try {
            val context = this
            setContent {
                val prefsViewModel: PreferencesViewModel = viewModel()
                val darkThemeState = prefsViewModel.getThemeMode(context).collectAsState().value
                val dynamicColorsState =
                    prefsViewModel.getDynamicColors(context).collectAsState().value
                val isInDarkTheme = when (darkThemeState) {
                    PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
                    PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
                    else -> isSystemInDarkTheme()
                }
                FoodExpirationDatesTheme(
                    darkTheme = isInDarkTheme,
                    dynamicColor = dynamicColorsState
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