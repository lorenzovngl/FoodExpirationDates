package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.util.FirebaseUtils
import com.lorenzovainigli.foodexpirationdates.util.OperationResult
import com.lorenzovainigli.foodexpirationdates.view.MainActivity

data class MenuItem(
    @DrawableRes val iconId: Int,
    val label: String,
    val onClick: () -> Unit = {}
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenMenu(
    activity: MainActivity? = null,
    searchQuery: MutableState<String>
) {
    val viewModel = activity?.viewModel
    val exportTaskSuccess = viewModel?.exportTaskSuccess?.value
    val operationResult = remember {
        mutableStateOf(OperationResult())
    }
    val notifyExportTaskDone = viewModel?.notifyExportTaskDone?.value
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val filePickerLauncher = when (activity) {
        null -> null
        else -> rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) {
            operationResult.value = viewModel?.importData(context, it)
                ?: OperationResult(state = OperationResult.State.NOT_PERFORMED)
        }
    }

    var isSearchBarExpanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(isSearchBarExpanded) {
        if (isSearchBarExpanded) {
            focusRequester.requestFocus()
        }
    }

    AnimatedVisibility(visible = isSearchBarExpanded.not()) {
        IconButton(
            onClick = {
                isSearchBarExpanded = true
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
    AnimatedVisibility(visible = isSearchBarExpanded) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    modifier = Modifier.focusRequester(focusRequester),
                    query = searchQuery.value,
                    expanded = isSearchBarExpanded,
                    onExpandedChange = { },
                    onQueryChange = { searchQuery.value = it },
                    onSearch = {},
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isSearchBarExpanded = false
                                searchQuery.value = ""
                            }
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    },
                    placeholder = { Text("Search...") },
                )
            },
            expanded = isSearchBarExpanded,
            onExpandedChange = { isSearchBarExpanded = it },
        ) {}
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
                label = stringResource(R.string.export_data),
                onClick = {
                    if (viewModel != null) {
                        viewModel.exportData(context)
                    } else {
                        FirebaseUtils.logToCrashlytics("Cannot export data, viewModel is null")
                    }
                    isExpanded = false
                }
            ),
            MenuItem(
                iconId = R.drawable.ic_import,
                label = stringResource(R.string.import_data),
                onClick = {
                    if (filePickerLauncher != null) {
                        filePickerLauncher.launch(arrayOf("*/*"))
                    } else {
                        FirebaseUtils.logToCrashlytics("Cannot import data, filePickerLauncher is null")
                    }
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
    if (notifyExportTaskDone == true) {
        if (exportTaskSuccess == true) {
//            SuccessDialog(
//                onDismiss = {
//                    viewModel.resetNotifyExportTaskDone()
//                },
//                message = stringResource(id = R.string.data_export_success)
//            )
        } else {
            ErrorDialog(
                onDismiss = {
                    viewModel.resetNotifyExportTaskDone()
                },
                message = stringResource(id = R.string.data_export_error)
            )
        }
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
