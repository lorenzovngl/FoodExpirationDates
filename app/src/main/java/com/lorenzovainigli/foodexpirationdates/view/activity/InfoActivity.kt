package com.lorenzovainigli.foodexpirationdates.view.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InfoActivityLayout

class InfoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoActivityLayout(
                context = this
            )
        }
    }

}