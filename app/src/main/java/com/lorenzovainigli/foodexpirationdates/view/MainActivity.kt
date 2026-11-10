package com.lorenzovainigli.foodexpirationdates.view

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.analytics.LocalAnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.scheduleDailyNotification
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.setupNotificationChannel
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewManager
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewModel: ExpirationDatesViewModel by viewModels()
    val foodListViewModel: FoodListViewModel by viewModels()
    val preferencesViewModel: PreferencesViewModel by viewModels()

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    @Inject
    lateinit var reviewManager: ReviewManager

    @Inject
    lateinit var preferencesRepository: PreferencesRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferencesRepository.migrateOnboardingPreferences()

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        preferencesRepository.checkAndSetSecureFlags(window)

        setupNotificationChannel(this)

        scheduleDailyNotification(
            context = this,
            hour = preferencesRepository.getUserNotificationTimeHour(),
            minute = preferencesRepository.getUserNotificationTimeMinute(),
            policy = ExistingWorkPolicy.KEEP
        )

        setContent {
            AppContent()
        }
    }

    @Composable
    private fun AppContent() {
        val prefsUiState by preferencesViewModel.uiState.collectAsStateWithLifecycle()

        val isInDarkTheme = when (prefsUiState.themeMode) {
            PreferencesRepository.ThemeMode.LIGHT -> false
            PreferencesRepository.ThemeMode.DARK -> true
            else -> isSystemInDarkTheme()
        }

        FoodExpirationDatesTheme(
            darkTheme = isInDarkTheme,
            dynamicColor = prefsUiState.dynamicColorsEnabled
        ) {
            AppNavigation()
        }
    }

    @Composable
    private fun AppNavigation() {
        val navController = rememberNavController()
        val showSnackbar = remember { mutableStateOf(false) }
        var isSearchActive by remember { mutableStateOf(false) }

        val startDestination = if (preferencesRepository.isOnboardingCompleted()) {
            Screen.MainScreen.route
        } else {
            Screen.OnboardingScreen.route
        }

        CompositionLocalProvider(
            LocalAnalyticsTracker provides analyticsTracker
        ) {
            MyScaffold(
                activity = this,
                navController = navController,
                startDestination = startDestination,
                showSnackbar = showSnackbar,
                onSearchIconClick = { isSearchActive = true }
            ) {
                Navigation(
                    activity = this,
                    showSnackbar = showSnackbar,
                    navController = navController,
                    startDestination = startDestination,
                    isSearchActive = isSearchActive,
                    onSearchBarClose = { isSearchActive = false },
                    reviewManager = reviewManager
                )
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        if (BuildConfig.DEBUG) {
            val preferences = newBase.getSharedPreferences(
                PreferencesRepository.SHARED_PREFS_NAME,
                Context.MODE_PRIVATE
            )

            val locale = preferences.getString(
                PreferencesRepository.KEY_LANGUAGE,
                Language.SYSTEM.code
            ) ?: Language.SYSTEM.code

            super.attachBaseContext(
                LocaleHelper.setLocale(newBase, locale)
            )
        } else {
            super.attachBaseContext(newBase)
        }
    }

}