package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.util.OperationResult

private sealed interface MenuIcon {
    data class Vector(val imageVector: ImageVector) : MenuIcon
    data class Resource(@DrawableRes val drawableId: Int) : MenuIcon
}

private data class MenuItem(
    val icon: MenuIcon,
    val label: String,
    val onClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenMenu(
    exportTaskSuccess: State<Boolean>?,
    notifyExportTaskDone: State<Boolean>?,
    onSearchClick: () -> Unit,
    onExportClick: () -> Unit,
    onImportClick: () -> Unit,
    onInfoClick: () -> Unit,
    onExportErrorDialogDismiss: () -> Unit,
) {
    val operationResult = remember {
        mutableStateOf(OperationResult())
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }

    IconButton(
        onClick = onSearchClick
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
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
                icon = MenuIcon.Resource(R.drawable.ic_export),
                label = stringResource(R.string.export_data),
                onClick = {
                    isExpanded = false
                    onExportClick()
                }
            ),
            MenuItem(
                icon =  MenuIcon.Resource(R.drawable.ic_import),
                label = stringResource(R.string.import_data),
                onClick = {
                    isExpanded = false
                    onImportClick()
                }
            ),
            MenuItem(
                icon = MenuIcon.Vector(Icons.Outlined.Info),
                label = stringResource(R.string.about_this_app),
                onClick = {
                    isExpanded = false
                    onInfoClick()
                }
            )
        ).forEach {
            DropdownMenuItem(
                leadingIcon = {
                    when (it.icon) {
                        is MenuIcon.Vector -> {
                            Icon(
                                imageVector = it.icon.imageVector,
                                contentDescription = it.label,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        is MenuIcon.Resource -> {
                            Icon(
                                painter = painterResource(it.icon.drawableId),
                                contentDescription = it.label,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                text = {
                    Text(it.label)
                },
                onClick = it.onClick
            )
        }
    }
    if (notifyExportTaskDone?.value == true && exportTaskSuccess?.value == false) {
        ErrorDialog(
            onDismiss = onExportErrorDialogDismiss,
            message = stringResource(id = R.string.data_export_error)
        )
    }
    when (operationResult.value.state) {
        OperationResult.State.FAILURE -> ErrorDialog(
            onDismiss = {
                operationResult.value = OperationResult()
            },
            message = operationResult.value.message
        )

        OperationResult.State.SUCCESS -> SuccessDialog(
            onDismiss = {
                operationResult.value = OperationResult()
            },
            message = operationResult.value.message
        )

        OperationResult.State.NOT_PERFORMED -> {}
    }

}

@Preview
@Composable
private fun MainScreenMenuPreview() {
    FoodExpirationDatesTheme {
        MainScreenMenu(
            exportTaskSuccess = remember { mutableStateOf(true) },
            notifyExportTaskDone =  remember { mutableStateOf(false) },
            onSearchClick = {},
            onExportClick = {},
            onImportClick = {},
            onInfoClick = {},
            onExportErrorDialogDismiss = {}
        )
    }
}
