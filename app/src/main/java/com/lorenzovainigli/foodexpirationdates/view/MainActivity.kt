package com.lorenzovainigli.foodexpirationdates.view

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingWorkPolicy
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.analytics.LocalAnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.scheduleDailyNotification
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.setupNotificationChannel
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewManager
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.viewmodel.FoodListViewModel
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
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
        enableEdgeToEdge()

        preferencesRepository.checkAndSetSecureFlags(window)

        setupNotificationChannel(this)
        scheduleDailyNotification(
            context = this,
            hour = preferencesRepository.getUserNotificationTimeHour(),
            minute = preferencesRepository.getUserNotificationTimeMinute(),
            policy = ExistingWorkPolicy.KEEP
        )
    }

    override fun onResume() {
        super.onResume()

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
                val navBarColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                val navBarColorArgb = navBarColor.toArgb()
                SideEffect {
                    window.navigationBarColor = navBarColorArgb
                }
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        lightScrim = android.graphics.Color.TRANSPARENT,
                        darkScrim = android.graphics.Color.TRANSPARENT,
                        detectDarkMode = { _ -> isInDarkTheme }
                    ),
                    navigationBarStyle = SystemBarStyle.auto(
                        lightScrim = navBarColorArgb,
                        darkScrim = navBarColorArgb,
                        detectDarkMode = { _ -> isInDarkTheme }
                    )
                )
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val showSnackbar = remember {
                        mutableStateOf(false)
                    }
                    var isSearchActive by remember { mutableStateOf(false) }
                    CompositionLocalProvider(
                        LocalAnalyticsTracker provides analyticsTracker
                    ) {
                        MyScaffold(
                            activity = this,
                            navController = navController,
                            showSnackbar = showSnackbar,
                            onSearchIconClick = { isSearchActive = true }
                        ) {
                            Navigation(
                                activity = this,
                                showSnackbar = showSnackbar,
                                navController = navController,
                                isSearchActive = isSearchActive,
                                onSearchBarClose = { isSearchActive = false },
                                reviewManager = reviewManager
                            )
                        }
                    }
                }
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