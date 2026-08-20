package com.lorenzovainigli.foodexpirationdates.playstore

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.lorenzovainigli.foodexpirationdates.PREVIEW_DEVICE

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen1FoodListScreenJapanese() = Screen1FoodListScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen2FoodEditorScreenJapanese() = Screen2FoodEditorScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen3BarcodeScannerScreenJapanese() = Screen3BarcodeScannerScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen4SettingsScreenJapanese() = Screen4SettingsScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen5InfoScreenJapanese() = Screen5InfoScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Screen6DarkThemeJapanese() = Screen6DarkThemeDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen7DynamicColorsJapanese() = Screen7DynamicColorsDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "ja",
    showBackground = true,
)
@Composable
fun Screen8MadeWithHeartScreenJapanese() = Screen8MadeWithHeartScreenDefault()
