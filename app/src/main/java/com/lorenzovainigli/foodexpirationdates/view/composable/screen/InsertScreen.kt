package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.Dropdown
import com.lorenzovainigli.foodexpirationdates.view.composable.MyDatePickerDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

const val TEST_TAG_INSERT_ITEM = "Insert item"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(
    activity: MainActivity? = null,
    navController: NavController,
    itemId: String? = null,
) {
    val itemToEdit = itemId?.let { activity?.viewModel?.getExpirationDate(it.toInt()) }
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
    var checkedOpeningDateState by remember {
        mutableStateOf(openingDate != null)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
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
        TextFieldDatePicker(
            datePickerState = datePickerExpDateState,
            label = stringResource(id = R.string.expiration_date)
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
            TextFieldDatePicker(
                modifier = Modifier
                    .clickable(
                        enabled = checkedOpeningDateState,
                        onClick = {}
                    )
                    .alpha(if (checkedOpeningDateState) 1f else 0.5f),
                datePickerState = datePickerOpeningDateState,
                label = stringResource(id = R.string.opening_date),
                allowUserInteraction = checkedOpeningDateState
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
                        readOnly = !checkedOpeningDateState,
                        onValueChange = {
                            timeSpan = try {
                                it.toInt()
                            } catch (_: Exception) {
                                0
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Dropdown(
                        modifier = Modifier.fillMaxWidth(),
                        choices = timeSpanArray,
                        onChange = {
                            dropdownChoice = it
                        },
                        allowUserInteraction = checkedOpeningDateState
                    )
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = { navController.popBackStack() },
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
                    .testTag(TEST_TAG_INSERT_ITEM)
                    .weight(0.5f)
                    .padding(top = 8.dp, start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                onClick = {
                    try {
                        if (foodName.isNotEmpty()) {
                            if (datePickerExpDateState.selectedDateMillis != null) {
                                var id = 0
                                if (itemToEdit != null) {
                                    id = itemId.toInt()
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
                                activity?.viewModel?.addExpirationDate(entry)
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    activity,
                                    R.string.please_select_a_date,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                activity,
                                R.string.please_enter_a_food_name,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDatePicker(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    label: String,
    allowUserInteraction: Boolean = true
) {
    var isDatePickerDialogOpen by remember {
        mutableStateOf(false)
    }
    TextField(
        modifier = modifier.clickable(onClick = {
            if (allowUserInteraction) {
                isDatePickerDialogOpen = true
            }
        }),
        enabled = false,
        label = {
            Text(
                text = label,
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
    if (isDatePickerDialogOpen) {
        MyDatePickerDialog(
            datePickerState = datePickerState,
            onConfirm = { isDatePickerDialogOpen = false },
            onDismiss = { isDatePickerDialogOpen = false }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@PreviewLightDark
@Composable
fun InsertScreenPreview() {
    FoodExpirationDatesTheme {
        Surface {
            InsertScreen(navController = rememberNavController())
        }
    }
}
