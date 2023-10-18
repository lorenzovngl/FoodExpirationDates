package com.lorenzovainigli.foodexpirationdates.view.composable.activity

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.composable.Dropdown
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertActivityLayout(
    context: Context = LocalContext.current,
    itemId: Int? = null,
    viewModel: ExpirationDatesViewModel? = viewModel(),
    prefsViewModel: PreferencesViewModel? = viewModel(),
    addExpirationDate: ((ExpirationDate) -> Unit)? = viewModel!!::addExpirationDate
) {
    val darkThemeState = prefsViewModel?.getThemeMode(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.ThemeMode.SYSTEM
    val dynamicColorsState = prefsViewModel?.getDynamicColors(context)?.collectAsState()?.value
        ?: false
    val isInDarkTheme = when (darkThemeState) {
        PreferencesRepository.Companion.ThemeMode.LIGHT.ordinal -> false
        PreferencesRepository.Companion.ThemeMode.DARK.ordinal -> true
        else -> isSystemInDarkTheme()
    }
    FoodExpirationDatesTheme(
        darkTheme = isInDarkTheme,
        dynamicColor = dynamicColorsState
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val activity = (LocalContext.current as? Activity)
            val itemToEdit = itemId?.let { viewModel?.getExpirationDate(it) }
            var foodNameToEdit = ""
            var expDate: Long? = null
            var openingDate: Long? = null
            if (itemToEdit != null) {
                foodNameToEdit = itemToEdit.foodName
                expDate = itemToEdit.expirationDate
                openingDate = itemToEdit.openingDate
            }
            var foodName by remember {
                mutableStateOf(foodNameToEdit)
            }
            var timeSpan by remember {
                mutableIntStateOf(itemToEdit?.timeSpanDays ?: 0)
            }
            val timeSpanArray = stringArrayResource(id = R.array.time_span)
            var dropdownChoice by remember {
                mutableStateOf(timeSpanArray[0])
            }
            val datePickerExpDateState = rememberDatePickerState(expDate)
            val datePickerOpeningDateState = rememberDatePickerState(openingDate)
            var isDatePickerDialogExpDateOpen by remember {
                mutableStateOf(false)
            }
            var checkedOpeningDateState by remember {
                mutableStateOf(openingDate != null)
            }
            var isDatePickerDialogOpeningDateOpen by remember {
                mutableStateOf(false)
            }
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
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
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior,
                        prefsViewModel = prefsViewModel
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
                containerColor = MaterialTheme.colorScheme.background,
                content = { padding ->
                    if (isDatePickerDialogExpDateOpen) {
                        DatePickerDialog(
                            dismissButton = {
                                OutlinedButton(
                                    onClick = { isDatePickerDialogExpDateOpen = false },
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
                                    modifier = Modifier.testTag("Insert date"),
                                    onClick = { isDatePickerDialogExpDateOpen = false },
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
                                    state = datePickerExpDateState
                                )
                            },
                            onDismissRequest = {
                                isDatePickerDialogExpDateOpen = false
                            }
                        )
                    }
                    if (isDatePickerDialogOpeningDateOpen) {
                        DatePickerDialog(
                            dismissButton = {
                                OutlinedButton(
                                    onClick = { isDatePickerDialogOpeningDateOpen = false },
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
                                    onClick = { isDatePickerDialogOpeningDateOpen = false },
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
                                    state = datePickerOpeningDateState
                                )
                            },
                            onDismissRequest = {
                                isDatePickerDialogOpeningDateOpen = false
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
                                onValueChange = {
                                    foodName = it
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TextField(
                                modifier = Modifier.clickable(onClick = {
                                    isDatePickerDialogExpDateOpen = true
                                }),
                                enabled = false,
                                label = {
                                    Text(
                                        text = stringResource(id = R.string.expiration_date),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                },
                                value = if (datePickerExpDateState.selectedDateMillis == null) "" else {
                                    val dateFormat = (DateFormat.getDateInstance(
                                        DateFormat.MEDIUM, Locale.getDefault()
                                    ) as SimpleDateFormat).toLocalizedPattern()
                                    val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                                    sdf.format(datePickerExpDateState.selectedDateMillis)
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
                            Spacer(modifier = Modifier.height(16.dp))
                            Column (
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10))
                                    .background(
                                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            TonalElevation.level2()
                                        )
                                    )
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = checkedOpeningDateState,
                                        onCheckedChange = { checkedOpeningDateState = it }
                                    )
                                    Text(
                                        text = "${stringResource(
                                            id = R.string.opening_date
                                        )} (${stringResource(
                                            id = R.string.optional
                                        )})",
                                    )
                                }
                                TextField(
                                    modifier = Modifier
                                        .clickable(
                                            enabled = checkedOpeningDateState,
                                            onClick = {
                                                isDatePickerDialogOpeningDateOpen = true
                                            }
                                        )
                                        .alpha(if (checkedOpeningDateState) 1f else 0.5f),
                                    enabled = false,
                                    label = {
                                        Text(
                                            text = stringResource(id = R.string.opening_date),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    },
                                    value = if (datePickerOpeningDateState.selectedDateMillis == null) "" else {
                                        val dateFormat = (DateFormat.getDateInstance(
                                            DateFormat.MEDIUM, Locale.getDefault()
                                        ) as SimpleDateFormat).toLocalizedPattern()
                                        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                                        sdf.format(datePickerOpeningDateState.selectedDateMillis)
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
                                Spacer(modifier = Modifier.height(16.dp))
                                Column(
                                    modifier = Modifier
                                        .alpha(if (checkedOpeningDateState) 1f else 0.5f)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                                        text = stringResource(id = R.string.time_span),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Row {
                                        TextField(
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            value = timeSpan.toString(),
                                            onValueChange = {
                                                timeSpan = try {
                                                    it.toInt()
                                                } catch (_: Exception) {
                                                    0
                                                }
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Number
                                            )
                                        )
                                        Dropdown(
                                            modifier = Modifier.fillMaxWidth(),
                                            choices = timeSpanArray,
                                            onChange = {
                                                dropdownChoice = it
                                            }
                                        )
                                    }
                                }
                            }
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
                                        .testTag("Insert item")
                                        .weight(0.5f)
                                        .padding(top = 8.dp, start = 4.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = MaterialTheme.colorScheme.onTertiary
                                    ),
                                    onClick = {
                                        try {
                                            if(foodName.isNotEmpty()) {
                                                if (datePickerExpDateState.selectedDateMillis != null) {
                                                    var id = 0
                                                    if (itemToEdit != null) {
                                                        id = itemId
                                                    }
                                                    val entry = ExpirationDate(
                                                        id = id,
                                                        foodName = foodName,
                                                        expirationDate = datePickerExpDateState.selectedDateMillis!!,
                                                        openingDate = if (checkedOpeningDateState) datePickerOpeningDateState.selectedDateMillis else null,
                                                        timeSpanDays = if (checkedOpeningDateState) {
                                                            timeSpan.let {
                                                                if (dropdownChoice == timeSpanArray[0]) { // Days
                                                                    timeSpan
                                                                } else { // Months
                                                                    timeSpan * 30
                                                                }
                                                            }
                                                        } else null
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
                                                Toast.makeText(activity,
                                                    R.string.please_enter_a_food_name,
                                                    Toast.LENGTH_SHORT).show()
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
fun InsertActivityLayoutPreview() {
    InsertActivityLayout(
        context = LocalContext.current,
        itemId = null,
        viewModel = null,
        prefsViewModel = null,
        addExpirationDate = null
    )
}