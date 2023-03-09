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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
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
    private var itemId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        itemId = intent.extras?.getInt("ITEM_ID")
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
        val itemToEdit = itemId?.let { viewModel?.getDate(it) }
        var foodNameToEdit = ""
        var expDate: Long? = null
        if (itemToEdit != null && viewModel != null) {
            foodNameToEdit = itemToEdit.foodName
            expDate = itemToEdit.expirationDate
        }
        var foodName by remember {
           mutableStateOf(foodNameToEdit)
        }
        val datePickerState = rememberDatePickerState(expDate)
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    if (itemToEdit != null)
                        Text(text = stringResource(id = R.string.edit_item))
                    else
                        Text(text = stringResource(id = R.string.add_item))
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
                                    text = stringResource(id = R.string.food_name),
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
                                Text(text = stringResource(id = R.string.expiration_date))
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
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            Button(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .padding(top = 8.dp, start = 4.dp),
                                onClick = {
                                    try {
                                        if (datePickerState.selectedDateMillis != null) {
                                            var id = 0
                                            if (itemToEdit != null && viewModel != null) {
                                                id = itemId ?: 0
                                            }
                                            val entry = ExpirationDate(
                                                id = id,
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
                                                R.string.please_select_a_date,
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
                                if (itemToEdit != null)
                                    Text(text = stringResource(id = R.string.update))
                                else
                                    Text(text = stringResource(id = R.string.insert))
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

    @Preview(locale = "it", showBackground = true)
    @Composable
    fun DialogInsertPreviewIT() {
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