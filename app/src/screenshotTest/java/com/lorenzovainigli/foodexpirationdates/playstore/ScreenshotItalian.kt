package com.lorenzovainigli.foodexpirationdates.playstore

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.lorenzovainigli.foodexpirationdates.PREVIEW_DEVICE

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen1FoodListScreenItalian() = Screen1FoodListScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen2FoodEditorScreenItalian() = Screen2FoodEditorScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen3BarcodeScannerScreenItalian() = Screen3BarcodeScannerScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen4SettingsScreenItalian() = Screen4SettingsScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen5InfoScreenItalian() = Screen5InfoScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Screen6DarkThemeItalian() = Screen6DarkThemeDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen7DynamicColorsItalian() = Screen7DynamicColorsDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "it",
    showBackground = true,
)
@Composable
fun Screen8MadeWithHeartScreenItalian() = Screen8MadeWithHeartScreenDefault()
