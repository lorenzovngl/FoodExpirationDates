package com.lorenzovainigli.foodexpirationdates.view

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.di.AppModule
import com.lorenzovainigli.foodexpirationdates.di.DaggerAppComponent
import com.lorenzovainigli.foodexpirationdates.model.PreferencesProvider
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDateViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
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
            InsertLayout()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InsertLayout(
        viewModel: ExpirationDateViewModel? = viewModel(),
        addExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::addExpirationDate
    ) {
        val context = LocalContext.current
        FoodExpirationDatesTheme(
            darkTheme = PreferencesProvider.getDarkTheme(context),
            dynamicColor = PreferencesProvider.getDynamicColors(context)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
                tonalElevation = TonalElevation.level2()
            ) {
                val activity = (LocalContext.current as? Activity)
                val itemToEdit = itemId?.let { viewModel?.getDate(it) }
                var foodNameToEdit = ""
                var expDate: Long? = null
                if (itemToEdit != null) {
                    foodNameToEdit = itemToEdit.foodName
                    expDate = itemToEdit.expirationDate
                }
                var foodName by remember {
                    mutableStateOf(foodNameToEdit)
                }
                val datePickerState = rememberDatePickerState(expDate)
                var isDialogOpen by remember {
                    mutableStateOf(false)
                }
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = stringResource(
                                id = if (itemToEdit != null) R.string.edit_item
                                else R.string.add_item
                            ),
                            navigationIcon = {
                                IconButton(onClick = { activity?.finish() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.ArrowBack,
                                        contentDescription = stringResource(id = R.string.back),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButtonPosition = FabPosition.End,
                    containerColor = MaterialTheme.colorScheme.background,
                    content = { padding ->
                        if (isDialogOpen) {
                            DatePickerDialog(
                                dismissButton = {
                                    OutlinedButton(
                                        onClick = { isDialogOpen = false },
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.tertiary
                                        ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.tertiary
                                        )
                                    ) {
                                        Text(text = stringResource(id = R.string.cancel))
                                    }
                                },
                                confirmButton = {
                                    Button(
                                        onClick = { isDialogOpen = false },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.tertiary,
                                            contentColor = MaterialTheme.colorScheme.onTertiary
                                        )
                                    ) {
                                        Text(text = stringResource(id = R.string.insert))
                                    }
                                },
                                content = {
                                    DatePicker(
                                        state = datePickerState
                                    )
                                },
                                onDismissRequest = {
                                    isDialogOpen = false
                                }
                            )
                        }
                        Column(Modifier.padding(padding)) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState())
                                    .padding(16.dp)
                            ) {
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
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        capitalization = KeyboardCapitalization.Sentences
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                TextField(
                                    modifier = Modifier.clickable(onClick = {
                                        isDialogOpen = true
                                    }),
                                    enabled = false,
                                    label = {
                                        Text(
                                            text = stringResource(id = R.string.expiration_date),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    value = if (datePickerState.selectedDateMillis == null) "" else {
                                        // TODO What kind of date format is the best here?
                                        val dateFormat = (DateFormat.getDateInstance(
                                            DateFormat.MEDIUM, Locale.getDefault()
                                        ) as SimpleDateFormat).toLocalizedPattern()
                                        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                                        sdf.format(datePickerState.selectedDateMillis)
                                    },
                                    onValueChange = {},
                                    colors = TextFieldDefaults.colors(
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        //For Icons
                                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                )
                                Row {
                                    OutlinedButton(
                                        onClick = { activity?.finish() },
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .padding(top = 8.dp, end = 4.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.tertiary
                                        ),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.Transparent,
                                            contentColor = MaterialTheme.colorScheme.tertiary
                                        ),
                                    ) {
                                        Text(text = stringResource(id = R.string.cancel))
                                    }
                                    Button(
                                        modifier = Modifier
                                            .weight(0.5f)
                                            .padding(top = 8.dp, start = 4.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.tertiary,
                                            contentColor = MaterialTheme.colorScheme.onTertiary
                                        ),
                                        onClick = {
                                            try {
                                                if(foodName.isNotEmpty()) {
                                                    if (datePickerState.selectedDateMillis != null) {
                                                        var id = 0
                                                        if (itemToEdit != null) {
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
                                                } else {
                                                    Toast.makeText(activity,R.string.please_enter_a_food_name,Toast.LENGTH_SHORT).show()
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
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        InsertLayout(null, null)
    }

    @Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
    @Composable
    fun PreviewDarkMode() {
        DefaultPreview()
    }

    @Preview(name = "Italian", locale = "it", showBackground = true)
    @Composable
    fun PreviewItalian() {
        DefaultPreview()
    }

    @Preview(name = "Arabic", locale = "ar", showBackground = true)
    @Composable
    fun PreviewArabic() {
        DefaultPreview()
    }

    @Preview(name = "German", locale = "de", showBackground = true)
    @Composable
    fun PreviewGerman() {
        DefaultPreview()
    }

    @Preview(name = "Hindi", locale = "hi", showBackground = true)
    @Composable
    fun PreviewHindi() {
        DefaultPreview()
    }

    @Preview(name = "Spanish", locale = "es", showBackground = true)
    @Composable
    fun PreviewSpanish() {
        DefaultPreview()
    }

}