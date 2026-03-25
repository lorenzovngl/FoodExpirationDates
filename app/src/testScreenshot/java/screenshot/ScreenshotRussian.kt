package screenshot

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class ScreenshotRussian : Screenshot() {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5.copy(
            locale = "ru"
        ),
        theme = "android:Theme.Material.Light.NoActionBar",
        useDeviceResolution = true
    )

    @Test
    fun screen1MainActivity() {
        super.screen1MainActivity(paparazzi)
    }

    @Test
    fun screen2InsertActivity() {
        super.screen2InsertActivity(paparazzi)
    }

    @Test
    fun screen3SettingsActivity() {
        super.screen3SettingsActivity(paparazzi)
    }

    @Test
    fun screen4InfoActivity() {
        super.screen4InfoActivity(paparazzi)
    }

    @Test
    fun screen5DarkMode() {
        super.screen5DarkMode(paparazzi)
    }

    @Test
    fun screen6DynamicColors(){
        super.screen6DynamicColors(paparazzi)
    }

    @Test
    fun screen7BarcodeScanner(){
        super.screen7BarcodeScanner(paparazzi)
    }

    @Test
    fun screen8MadeWithHeart(){
        super.screen8MadeWithHeart(paparazzi)
    }
}