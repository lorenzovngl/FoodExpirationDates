package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimeBottomSheet(
    timePickerState: TimePickerState = rememberTimePickerState(11, 0, true),
    onDismissRequest: () -> Unit = {}
){
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        ),
        content = {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(4.dp)
            ) {
                TimePicker(
                    state = timePickerState
                )
            }
        },
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotificationTimeBottomSheetPreview(){
    NotificationTimeBottomSheet()
}