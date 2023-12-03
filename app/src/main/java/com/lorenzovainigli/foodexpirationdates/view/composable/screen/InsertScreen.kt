package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(
    activity: MainActivity? = null,
    navController: NavController,
    itemId: String? = null,
) {
    val itemToEdit = itemId?.let { activity?.viewModel?.getDate(it.toInt()) }
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
                    modifier = Modifier.testTag("Insert date"),
                    onClick = { isDialogOpen = false },
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.tertiary,
//                        contentColor = MaterialTheme.colorScheme.onTertiary
//                    )
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
                    .testTag("Insert item")
                    .weight(0.5f)
                    .padding(top = 8.dp, start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                onClick = {
                    try {
                        if (foodName.isNotEmpty()) {
                            if (datePickerState.selectedDateMillis != null) {
                                var id = 0
                                if (itemToEdit != null) {
                                    id = itemId.toInt()
                                }
                                val entry = ExpirationDate(
                                    id = id,
                                    foodName = foodName,
                                    expirationDate = datePickerState.selectedDateMillis!!
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
