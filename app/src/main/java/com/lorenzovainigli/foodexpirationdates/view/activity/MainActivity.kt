package com.lorenzovainigli.foodexpirationdates.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.di.AppModule
import com.lorenzovainigli.foodexpirationdates.di.DaggerAppComponent
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.MainActivityLayout
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
        NotificationManager.setupNotificationChannel(this)
    }

    override fun onResume() {
        super.onResume()
        setContent {
            val viewModel: ExpirationDatesViewModel = viewModel()
            val items by viewModel.getDates().collectAsState(emptyList())
            MainActivityLayout(
                context = this,
                items = items,
                viewModel = viewModel
            )
        }
    }

}