package com.lorenzovainigli.foodexpirationdates.view.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.UISettingsActivityLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UISettingsActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UISettingsActivityLayout()
        }
    }
}