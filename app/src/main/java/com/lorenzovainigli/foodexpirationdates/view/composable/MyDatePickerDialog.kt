package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import java.util.Calendar

const val TEST_TAG_INSERT_DATE = "Insert date"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePickerDialog(
    datePickerState: DatePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    DatePickerDialog(
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
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
                modifier = Modifier.testTag(TEST_TAG_INSERT_DATE),
                onClick = onConfirm,
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
        onDismissRequest = onDismiss
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@PreviewLightDark
fun MyDatePickerDialogPreview(){
    FoodExpirationDatesTheme {
        Surface {
            MyDatePickerDialog(
                datePickerState = rememberDatePickerState(Calendar.getInstance().time.time),
                onConfirm = { },
                onDismiss = { }
            )
        }
    }
}