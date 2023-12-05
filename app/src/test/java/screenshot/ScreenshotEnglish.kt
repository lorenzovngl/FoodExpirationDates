package screenshot

import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import org.junit.Rule
import org.junit.Test

class ScreenshotEnglish : Screenshot() {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5.copy(
            locale = "en"
        ),
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    /*private val maxImageSize = 2_000

    @Before
    fun initMaxImageSize() {
        maxImageSize.let {
            app.cash.paparazzi.internal.ImageUtils::class.java.declaredFields.firstOrNull {
                it.name == "THUMBNAIL_SIZE"
            }?.let {
                it.isAccessible = true
                it.set(paparazzi, maxImageSize)
            }
        }
    }*/

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

}