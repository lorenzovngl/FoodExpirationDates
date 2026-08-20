package com.lorenzovainigli.foodexpirationdates.playstore

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.lorenzovainigli.foodexpirationdates.PREVIEW_DEVICE

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen1FoodListScreenSpanish() = Screen1FoodListScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen2FoodEditorScreenSpanish() = Screen2FoodEditorScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen3BarcodeScannerScreenSpanish() = Screen3BarcodeScannerScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen4SettingsScreenSpanish() = Screen4SettingsScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen5InfoScreenSpanish() = Screen5InfoScreenDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun Screen6DarkThemeSpanish() = Screen6DarkThemeDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen7DynamicColorsSpanish() = Screen7DynamicColorsDefault()

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    locale = "es",
    showBackground = true,
)
@Composable
fun Screen8MadeWithHeartScreenSpanish() = Screen8MadeWithHeartScreenDefault()
