package com.lorenzovainigli.foodexpirationdates.playstore

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.lorenzovainigli.foodexpirationdates.PREVIEW_DEVICE
import com.lorenzovainigli.foodexpirationdates.PlayStoreScreenshot
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.screen.BarcodeScannerScreenPreview
import com.lorenzovainigli.foodexpirationdates.screen.FoodEditorScreenPreview
import com.lorenzovainigli.foodexpirationdates.screen.InfoScreenPreview
import com.lorenzovainigli.foodexpirationdates.screen.FoodListScreenPreview
import com.lorenzovainigli.foodexpirationdates.screen.MadeWithHeartScreenPreview
import com.lorenzovainigli.foodexpirationdates.screen.SettingsScreenPreview
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
)
@Composable
fun Screen1FoodListScreenDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[0]
    ) {
        FoodListScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
)
@Composable
fun Screen2FoodEditorScreenDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[1],
        navigationBarElevation = TonalElevation.level0()
    ) {
        FoodEditorScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
)
@Composable
fun Screen3BarcodeScannerScreenDefault(){
    PlayStoreScreenshot(
        text = stringResource(id = R.string.screenshot_titles_barcode_scanner),
        statusBarAsOverlay = true,
        navigationBarElevation = TonalElevation.level0()
    ) {
        BarcodeScannerScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
)
@Composable
fun Screen4SettingsScreenDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[2]
    ) {
        SettingsScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
)
@Composable
fun Screen5InfoScreenDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[3]
    ) {
        InfoScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun Screen6DarkThemeDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[4],
        darkTheme = true
    ) {
        FoodListScreenPreview()
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true
)
@Composable
fun Screen7DynamicColorsDefault(){
    PlayStoreScreenshot(
        text = stringArrayResource(id = R.array.screenshot_titles)[5],
        dynamicColors = true
    ) {
        FoodListScreenPreview(dynamicColors = true)
    }
}

@PreviewTest
@Preview(
    device = PREVIEW_DEVICE,
    showBackground = true
)
@Composable
fun Screen8MadeWithHeartScreenDefault(){
    PlayStoreScreenshot(
        text = stringResource(id = R.string.screenshot_titles_made_with_heart),
        navigationBarElevation = TonalElevation.level0()
    ) {
        MadeWithHeartScreenPreview()
    }
}
