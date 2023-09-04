package screenshot.night

import androidx.compose.ui.platform.LocalContext
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.android.resources.NightMode
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InfoActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InsertActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.MainActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.SettingsActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.getItemsForPreview
import org.junit.Rule
import org.junit.Test
import screenshot.Screenshot

class ScreenshotItalianNight : Screenshot {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5.copy(
            locale = "it",
            nightMode = NightMode.NIGHT
        ),
        theme = "android:Theme.Material.Light.NoActionBar"
    )

    @Test
    override fun screen1MainActivity() {
        paparazzi.snapshot {
            MainActivityLayout(
                items = getItemsForPreview(LocalContext.current),
                viewModel = null,
                addExpirationDate = null,
                deleteExpirationDate = null
            )
        }
    }

    @Test
    override fun screen2InsertActivity() {
        paparazzi.snapshot {
            InsertActivityLayout(
                itemId = null,
                viewModel = null,
                addExpirationDate = null
            )
        }
    }

    @Test
    override fun screen3SettingsActivity() {
        paparazzi.snapshot {
            SettingsActivityLayout()
        }
    }

    @Test
    override fun screen4InfoActivity() {
        paparazzi.snapshot {
            InfoActivityLayout()
        }
    }

}