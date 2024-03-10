package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun SuccessDialog(
    onDismiss: () -> Unit = {},
    message: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(stringResource(id = R.string.ok))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = Icons.Default.CheckCircle.name
            )
        },
        text = {
            Text(message)
        },
        iconContentColor = MaterialTheme.colorScheme.onErrorContainer.copy(
            red = MaterialTheme.colorScheme.onErrorContainer.green,
            green = MaterialTheme.colorScheme.onErrorContainer.red
        )
    )
}

@Composable
fun ErrorDialog(
    onDismiss: () -> Unit = {},
    message: String
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors().copy(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.errorContainer,
                )
            ) {
                Text(stringResource(id = R.string.ok))
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = Icons.Default.Warning.name
            )
        },
        text = {
            Text(message)
        },
        containerColor = MaterialTheme.colorScheme.errorContainer,
        iconContentColor = MaterialTheme.colorScheme.onErrorContainer,
        textContentColor = MaterialTheme.colorScheme.onErrorContainer
    )
}

@PreviewLightDark
@Composable
fun ExportSuccessDialogPreview() {
    FoodExpirationDatesTheme {
        Surface {
            SuccessDialog(message = "Success message")
        }
    }
}

@PreviewLightDark
@Composable
fun ExportFailedDialogPreview() {
    FoodExpirationDatesTheme {
        Surface {
            ErrorDialog(message = "Error message")
        }
    }
}