package com.lorenzovainigli.foodexpirationdates.view.composable.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.AutoResizedText
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.NotificationTimeBottomSheet
import com.lorenzovainigli.foodexpirationdates.view.composable.SettingsItem
import java.text.SimpleDateFormat
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    activity: MainActivity? = null
) {
    val context = LocalContext.current
    val prefsViewModel = activity?.preferencesViewModel
    val darkThemeState = prefsViewModel?.getThemeMode(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.ThemeMode.SYSTEM
    val dynamicColorsState = prefsViewModel?.getDynamicColors(context)?.collectAsState()?.value
        ?: false
    val topBarFontState = prefsViewModel?.getTopBarFont(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal

    val dateFormat = prefsViewModel?.getDateFormat(context)?.collectAsState()?.value
        ?: PreferencesRepository.getAvailOtherDateFormats()[0]
    var sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
    var isDateFormatDialogOpened by remember {
        mutableStateOf(false)
    }

    val notificationTimeHour =
        prefsViewModel?.getNotificationTimeHour(context)?.collectAsState()?.value
            ?: 11
    val notificationTimeMinute =
        prefsViewModel?.getNotificationTimeMinute(context)?.collectAsState()?.value
            ?: 0
    val timePickerState =
        rememberTimePickerState(notificationTimeHour, notificationTimeMinute, true)
    var isNotificationTimeBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    prefsViewModel?.let {
        DateFormatDialog(
            isDialogOpen = isDateFormatDialogOpened,
            onDismissRequest = {
                sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
                isDateFormatDialogOpened = false
            },
            onClickDate = it::setDateFormat
        )
    }
    if (isNotificationTimeBottomSheetOpen) {
        NotificationTimeBottomSheet(
            timePickerState = timePickerState,
            onDismissRequest = {
                prefsViewModel?.setNotificationTime(
                    context,
                    timePickerState.hour, timePickerState.minute
                )
                NotificationManager.scheduleDailyNotification(
                    context,
                    timePickerState.hour,
                    timePickerState.minute
                )
                isNotificationTimeBottomSheetOpen = false
            }
        )
    }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.behaviour),
            style = MaterialTheme.typography.labelLarge
        )
        SettingsItem(
            label = stringResource(id = R.string.date_format)
        ) {
            ClickableText(
                modifier = Modifier.testTag(stringResource(id = R.string.date_format)),
                text = AnnotatedString(sdf.format(Calendar.getInstance().time)),
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                onClick = {
                    isDateFormatDialogOpened = true
                }
            )
        }
        SettingsItem(
            label = stringResource(R.string.notification_time)
        ) {
            var text = ""
            if (timePickerState.hour < 10) {
                text += "0"
            }
            text = timePickerState.hour.toString() + ":"
            if (timePickerState.minute < 10) {
                text += "0"
            }
            text += timePickerState.minute.toString()
            ClickableText(
                modifier = Modifier.testTag("Notification time"),
                text = AnnotatedString(text),
                style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                onClick = {
                    isNotificationTimeBottomSheetOpen = true
                }
            )
        }
        Text(
            text = stringResource(R.string.appearance),
            style = MaterialTheme.typography.labelLarge
        )
        SettingsItem(
            label = stringResource(R.string.theme)
        ) {
            PreferencesRepository.Companion.ThemeMode.entries.forEach {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f)
                )
                if (it.ordinal == darkThemeState) {
                    Button(onClick = {}) {
                        AutoResizedText(
                            text = context.getString(it.label)
                        )
                    }
                }
                if (it.ordinal != darkThemeState) {
                    OutlinedButton(
                        onClick = {
                            prefsViewModel?.setThemeMode(context, it)
                        },
                    ) {
                        AutoResizedText(
                            text = context.getString(it.label)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f)
                )
            }
        }
        SettingsItem(
            label = stringResource(R.string.dynamic_colors)
        ) {
            Spacer(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Switch(
                checked = dynamicColorsState,
                onCheckedChange = {
                    prefsViewModel?.setDynamicColors(context, it)
                }
            )
        }
        SettingsItem(
            label = stringResource(R.string.top_bar_font_style)
        ) {
            PreferencesRepository.Companion.TopBarFont.entries.forEach { topBarFont ->
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f)
                )
                if (topBarFont.ordinal != topBarFontState) {
                    OutlinedButton(
                        onClick = {
                            prefsViewModel?.setTopBarFont(context, topBarFont)
                        },
                    ) {
                        AutoResizedText(
                            text = context.getString(topBarFont.label)
                        )
                    }
                }
                if (topBarFont.ordinal == topBarFontState) {
                    Button(onClick = {}) {
                        AutoResizedText(
                            text = context.getString(topBarFont.label)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}