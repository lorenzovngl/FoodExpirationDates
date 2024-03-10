package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel

data class MenuItem(
    @DrawableRes val iconId: Int,
    val label: String,
    val onClick: () -> Unit = {}
)

@Composable
fun MainScreenMenu() {
    val viewModel: ExpirationDatesViewModel = viewModel()
    val exportTaskSuccess = viewModel.exportTaskSuccess.value
    val notifyExportTaskDone = viewModel.notifyExportTaskDone.value
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    IconButton(
        onClick = { isExpanded = true }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(id = R.string.back),
            tint = MaterialTheme.colorScheme.primary
        )
    }
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = {
            isExpanded = false
        }
    ) {
        arrayOf(
            MenuItem(
                iconId = R.drawable.ic_export,
                label = "Export data",
                onClick = {
                    viewModel.exportData(context)
                    isExpanded = false
                }
            )
        ).forEach {
            DropdownMenuItem(
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = it.iconId),
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                text = {
                    Text(it.label)
                },
                onClick = it.onClick
            )
        }
    }
    if (notifyExportTaskDone) {
        if (exportTaskSuccess) {
            SuccessDialog(
                onDismiss = {
                    viewModel.resetNotifyExportTaskDone()
                },
                message = stringResource(id = R.string.data_export_success)
            )
        } else {
            ErrorDialog(
                onDismiss = {
                    viewModel.resetNotifyExportTaskDone()
                },
                message = stringResource(id = R.string.data_export_error)
            )
        }
    }
}
