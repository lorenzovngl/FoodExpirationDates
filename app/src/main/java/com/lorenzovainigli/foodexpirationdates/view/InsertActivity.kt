package com.lorenzovainigli.foodexpirationdates.view

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.di.AppModule
import com.lorenzovainigli.foodexpirationdates.di.DaggerAppComponent
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class InsertActivity : ComponentActivity() {

    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
        setContent {
            FoodExpirationDatesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InsertLayout()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InsertLayout(
        viewModel: ExpirationDateViewModel? = viewModel(),
        addExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::addExpirationDate
    ) {
        val activity = (LocalContext.current as? Activity)
        var foodName by remember { mutableStateOf("") }
        val datePickerState = rememberDatePickerState()
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = "Add item")
                }) },
            floatingActionButtonPosition = FabPosition.End,
            content = { padding ->
                Column(Modifier.padding(padding)) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        TextField(
                            label = {
                                Text(
                                    text = "Food name",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            value = foodName,
                            onValueChange = { newText ->
                                foodName = newText
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        DatePicker(
                            title = {
                                Text(text = "Expiration Date")
                            },
                            state = datePickerState
                        )
                        Row {
                            OutlinedButton(
                                onClick = { activity?.finish() },
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(top = 8.dp, end = 4.dp)
                            ) {
                                Text(text = "Cancel")
                            }
                            Button(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(top = 8.dp, start = 4.dp),
                                onClick = {
                                    try {
                                        if (datePickerState.selectedDateMillis != null) {
                                            val entry = ExpirationDate(
                                                id = 0,
                                                foodName = foodName,
                                                expirationDate = datePickerState.selectedDateMillis!!
                                            )
                                            if (addExpirationDate != null) {
                                                addExpirationDate(entry)
                                            }
                                            activity?.finish()
                                        } else {
                                            Toast.makeText(
                                                activity,
                                                "Please select a date",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            activity,
                                            e.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            ) {
                                Text(text = "Insert")
                            }
                        }
                    }
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DialogInsertPreview() {
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                InsertLayout(null, null)
            }
        }
    }

    @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun DialogInsertPreviewNight() {
        FoodExpirationDatesTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                InsertLayout(null, null)
            }
        }
    }

}