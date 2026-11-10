package com.lorenzovainigli.foodexpirationdates.onboarding.pages

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.NotificationTimeBottomSheet
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTimePage(
    initialHour: Int,
    initialMinute: Int,
    onTimeSelected: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = DateFormat.is24HourFormat(context)
    )

    val displayedTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, timePickerState.hour)
        set(Calendar.MINUTE, timePickerState.minute)
        set(Calendar.SECOND, 0)
    }

    val formattedTime = DateFormat.getTimeFormat(context)
        .format(displayedTime.time)

    var isNotificationTimeBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    if (isNotificationTimeBottomSheetOpen) {
        NotificationTimeBottomSheet(
            timePickerState = timePickerState,
            onDismissRequest = {
                onTimeSelected(
                    timePickerState.hour,
                    timePickerState.minute
                )
                isNotificationTimeBottomSheetOpen = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Schedule,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(64.dp)
        )

        Text(
            text = stringResource(
                R.string.onboarding_time_title
            ),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = stringResource(
                R.string.onboarding_time_description
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            onClick = { isNotificationTimeBottomSheetOpen = true }
        ) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationTimePagePreview() {
    FoodExpirationDatesTheme {
        NotificationTimePage(
            initialHour = 11,
            initialMinute = 0,
            onTimeSelected = { _, _ -> }
        )
    }
}
