package com.lorenzovainigli.foodexpirationdates.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.checkAndSetSecureFlags
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: ExpirationDatesViewModel by viewModels()
    val preferencesViewModel: PreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { viewModel.isSplashScreenLoading.value }

        checkAndSetSecureFlags(context = this, window)

        NotificationManager.setupNotificationChannel(this)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        val context = this

        setContent {

            val prefsViewModel: PreferencesViewModel = viewModel()
            val darkThemeState = prefsViewModel.getThemeMode(context).collectAsState().value
            val dynamicColorsState = prefsViewModel.getDynamicColors(context).collectAsState().value
            val isInDarkTheme = when (darkThemeState) {
                PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
                PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
                else -> isSystemInDarkTheme()
            }
            val systemBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT,
                detectDarkMode = { _ -> isInDarkTheme }
            )
            enableEdgeToEdge(
                statusBarStyle = systemBarStyle,
                navigationBarStyle = systemBarStyle
            )
            FoodExpirationDatesTheme(
                darkTheme = isInDarkTheme,
                dynamicColor = dynamicColorsState
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val showSnackbar = remember {
                        mutableStateOf(false)
                    }
                    MyScaffold(
                        activity = this,
                        navController = navController,
                        showSnackbar = showSnackbar
                    ) {
                        Navigation(
                            activity = this,
                            navController = navController,
                            showSnackbar = showSnackbar
                        )
                    }
                }
            }
        }
    }

}