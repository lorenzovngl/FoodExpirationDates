package com.lorenzovainigli.foodexpirationdates.view.composable.activity

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.PreferencesProvider
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar
import com.lorenzovainigli.foodexpirationdates.view.composable.NotificationTimeBottomSheet
import com.lorenzovainigli.foodexpirationdates.view.composable.SettingsItem
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import java.text.SimpleDateFormat
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsActivityLayout(
    scheduleDailyNotification: ((Int, Int) -> Unit)? = null
) {
    val context = LocalContext.current
    val activity = (LocalContext.current as? Activity)
    var dateFormat = PreferencesProvider.getUserDateFormat(context)
    var sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
    var isDateFormatDialogOpened by remember {
        mutableStateOf(false)
    }
    val notificationTimeHour = PreferencesProvider.getUserNotificationTimeHour(context)
    val notificationTimeMinute = PreferencesProvider.getUserNotificationTimeMinute(context)
    val timePickerState =
        rememberTimePickerState(notificationTimeHour, notificationTimeMinute, true)
    var isNotificationTimeBottomSheetOpen by remember {
        mutableStateOf(false)
    }
    var darkThemeState by remember {
        mutableStateOf(PreferencesProvider.getThemeMode(context))
    }
    var dynamicColorsState by remember {
        mutableStateOf(PreferencesProvider.getDynamicColors(context))
    }
    FoodExpirationDatesTheme(
        darkTheme = when (darkThemeState) {
            PreferencesProvider.Companion.ThemeMode.LIGHT.ordinal -> false
            PreferencesProvider.Companion.ThemeMode.DARK.ordinal -> true
            else -> isSystemInDarkTheme()
        },
        dynamicColor = dynamicColorsState
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold(
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.settings),
                        navigationIcon = {
                            IconButton(onClick = { activity?.finish() }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                DateFormatDialog(
                    isDialogOpen = isDateFormatDialogOpened,
                    onDismissRequest = {
                        dateFormat = PreferencesProvider.getUserDateFormat(context)
                        sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
                        isDateFormatDialogOpened = false
                    }
                )
                if (isNotificationTimeBottomSheetOpen) {
                    NotificationTimeBottomSheet(
                        timePickerState = timePickerState,
                        onDismissRequest = {
                            PreferencesProvider.setUserNotificationTime(
                                context,
                                timePickerState.hour, timePickerState.minute
                            )
                            if (scheduleDailyNotification != null) {
                                scheduleDailyNotification(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )
                            }
                            isNotificationTimeBottomSheetOpen = false
                        }
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SettingsItem(
                        label = stringResource(id = R.string.date_format)
                    ){
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
                    ){
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
                    SettingsItem(
                        label = stringResource(R.string.theme)
                    ){
                        PreferencesProvider.Companion.ThemeMode.values().forEach {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(0.1f)
                            )
                            if (it.ordinal == darkThemeState) {
                                Button(onClick = {}) {
                                    Text(
                                        text = context.getString(it.label)
                                    )
                                }
                            }
                            if(it.ordinal != darkThemeState){
                                OutlinedButton(
                                    onClick = {
                                        darkThemeState = it.ordinal
                                        PreferencesProvider.setThemeMode(context, it)
                                    },
                                ) {
                                    Text(
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
                    ){
                        Spacer(
                            Modifier
                                .weight(1f)
                                .fillMaxHeight())
                        Switch(
                            checked = dynamicColorsState,
                            onCheckedChange = {
                                PreferencesProvider.setDynamicColors(context, it)
                                dynamicColorsState = it
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@DefaultPreviews
@LanguagePreviews
@Composable
fun SettingsActivityLayoutPreview() {
    SettingsActivityLayout()
}