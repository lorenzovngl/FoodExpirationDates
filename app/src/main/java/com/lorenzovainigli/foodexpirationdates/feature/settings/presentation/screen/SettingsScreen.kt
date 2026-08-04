package com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.screen

import android.app.Activity
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.settings.presentation.model.SettingsUiState
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.ThemeMode
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.TopBarFont
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.getScreenProtectionEnabled
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository.Companion.setScreenProtectionEnabled
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.util.areNotificationsEnabled
import com.lorenzovainigli.foodexpirationdates.util.openNotificationSettings
import com.lorenzovainigli.foodexpirationdates.view.composable.AutoResizedText
import com.lorenzovainigli.foodexpirationdates.view.composable.DateFormatDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.LanguagePickerDialog
import com.lorenzovainigli.foodexpirationdates.view.composable.NotificationDisabledBanner
import com.lorenzovainigli.foodexpirationdates.view.composable.NotificationTimeBottomSheet
import com.lorenzovainigli.foodexpirationdates.view.composable.SettingsItem
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onDateFormatChange: (String) -> Unit,
    onNotificationTimeChange: (Int, Int) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorsChange: (Boolean) -> Unit,
    onTopBarFontChange: (TopBarFont) -> Unit,
    onMonochromeIconsChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    var sdf = SimpleDateFormat(
        state.dateFormat,
        configuration.locales[0]
    )

    var isDateFormatDialogOpened by remember {
        mutableStateOf(false)
    }
    var isLanguagePickerDialogOpened by remember {
        mutableStateOf(false)
    }

    val timePickerState = rememberTimePickerState(
        initialHour = state.notificationHour,
        initialMinute = state.notificationHour,
        is24Hour = true
    )

    var isNotificationTimeBottomSheetOpen by remember {
        mutableStateOf(false)
    }

    var isScreenProtectionEnabled by remember { mutableStateOf(getScreenProtectionEnabled(context)) }

    DateFormatDialog(
        isDialogOpen = isDateFormatDialogOpened,
        onDismissRequest = {
            sdf = SimpleDateFormat(
                state.dateFormat,
                configuration.locales[0]
            )
            isDateFormatDialogOpened = false
        },
        onClickDate = { _, string ->
            onDateFormatChange(string)
        }
    )

    if (isNotificationTimeBottomSheetOpen) {
        NotificationTimeBottomSheet(
            timePickerState = timePickerState,
            onDismissRequest = {
                onNotificationTimeChange(
                    timePickerState.hour,
                    timePickerState.minute
                )
                isNotificationTimeBottomSheetOpen = false
            }
        )
    }
    LanguagePickerDialog(
        isDialogOpen = isLanguagePickerDialogOpened,
        onDismiss = {
            isLanguagePickerDialogOpened = false
        }
    )
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        val context = LocalContext.current
        var showPermissionBanner by remember {
            mutableStateOf(!areNotificationsEnabled(context))
        }
        LifecycleResumeEffect(Unit) {
            showPermissionBanner = !areNotificationsEnabled(context)
            onPauseOrDispose { }
        }
        if (showPermissionBanner) {
            NotificationDisabledBanner(
                onSettingsClick = {
                    openNotificationSettings(context)
                }
            )
        }

        Text(
            text = stringResource(R.string.behaviour),
            style = MaterialTheme.typography.labelLarge
        )

        SettingsItem(
            label = stringResource(id = R.string.date_format),
            description = stringResource(id = R.string.date_format_desc)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicText(
                    modifier = Modifier.testTag(stringResource(id = R.string.date_format))
                        .clickable { isDateFormatDialogOpened = true },
                    text = AnnotatedString(sdf.format(Calendar.getInstance().time)),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change date format",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        SettingsItem(
            label = stringResource(id = R.string.notification_time),
            description = stringResource(id = R.string.notification_time_desc)
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BasicText(
                    modifier = Modifier.testTag("Notification time")
                        .clickable { isNotificationTimeBottomSheetOpen = true },
                    text = AnnotatedString(text),
                    style = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Change notification time",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(stringResource(R.string.privacy),
            style = MaterialTheme.typography.labelLarge
        )
        SettingsItem(
            label = stringResource(R.string.enable_screen_protection),
            description = stringResource(R.string.protect_your_screen)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 11.dp)
            ) {
                Switch(
                    modifier = Modifier.padding(start = 4.dp),
                    checked = isScreenProtectionEnabled,
                    onCheckedChange = { enabled ->
                        isScreenProtectionEnabled = enabled

                        setScreenProtectionEnabled(context, enabled)

                        if (enabled) {
                            (context as Activity).window.setFlags(
                                WindowManager.LayoutParams.FLAG_SECURE,
                                WindowManager.LayoutParams.FLAG_SECURE
                            )
                            Log.d("isScreenProtectionEnabled", "Screen protection enabled: true")
                        } else {
                            (context as Activity).window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                            Log.d("isScreenProtectionEnabled", "Screen protection enabled: false")
                        }
                    }
                )
            }
        }

        Text(
            text = stringResource(R.string.appearance),
            style = MaterialTheme.typography.labelLarge
        )
        SettingsItem(
            label = stringResource(R.string.theme)
        ) {
            ThemeMode.entries.forEach {
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f)
                )
                if (it.ordinal == state.themeMode.ordinal) {
                    Button(onClick = {}) {
                        AutoResizedText(
                            text = stringResource(it.label)
                        )
                    }
                }
                if (it.ordinal != state.themeMode.ordinal) {
                    OutlinedButton(
                        onClick = {
                            onThemeModeChange(it)
                        },
                    ) {
                        AutoResizedText(
                            text = stringResource(it.label)
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
                checked = state.dynamicColorsEnabled,
                onCheckedChange = {
                    onDynamicColorsChange(it)
                }
            )
        }
        SettingsItem(
            label = stringResource(R.string.top_bar_font_style)
        ) {
            TopBarFont.entries.forEach { topBarFont ->
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(0.1f)
                )
                if (topBarFont.ordinal != state.topBarFont.ordinal) {
                    OutlinedButton(
                        onClick = {
                            onTopBarFontChange(topBarFont)
                        },
                    ) {
                        AutoResizedText(
                            text = stringResource(topBarFont.label)
                        )
                    }
                }
                if (topBarFont.ordinal == state.topBarFont.ordinal) {
                    Button(onClick = {}) {
                        AutoResizedText(
                            text = stringResource(topBarFont.label)
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
            label = stringResource(R.string.monochrome_icons)
        ) {
            Spacer(
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            Switch(
                checked = state.monochromeIconsEnabled,
                onCheckedChange = {
                    onMonochromeIconsChange(it)
                }
            )
        }

        if (BuildConfig.DEBUG) {
            Text(
                text = stringResource(R.string.debug_options),
                style = MaterialTheme.typography.labelLarge
            )
            SettingsItem(
                label = stringResource(R.string.language)
            ) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BasicText(
                        modifier = Modifier.clickable {
                            isLanguagePickerDialogOpened = true
                        },
                        text = Language.fromCode(PreferencesRepository.getLanguage(context)).label,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Change language",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@PreviewLightDark
@LanguagePreviews
@PreviewScreenSizes
@Composable
fun SettingsScreenContentPreview() {
    FoodExpirationDatesTheme {
        Surface {
            SettingsScreen(
                state = SettingsUiState(),
                onDateFormatChange = {},
                onNotificationTimeChange = { _, _ -> },
                onThemeModeChange = {},
                onDynamicColorsChange = {},
                onTopBarFontChange = {},
                onMonochromeIconsChange = {}
            )
        }
    }
}
