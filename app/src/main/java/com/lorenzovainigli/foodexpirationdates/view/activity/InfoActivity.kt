package com.lorenzovainigli.foodexpirationdates.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InfoActivityLayout

class InfoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            InfoActivityLayout(
                context = this
            )
        }
    }

}