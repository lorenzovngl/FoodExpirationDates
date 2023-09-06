package screenshot

import androidx.compose.ui.platform.LocalContext
import app.cash.paparazzi.Paparazzi
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InfoActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.InsertActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.MainActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.SettingsActivityLayout
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.getItemsForPreview

open class Screenshot {

    fun screen1MainActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            MainActivityLayout(
                items = getItemsForPreview(LocalContext.current),
                viewModel = null,
                prefsViewModel = null,
                addExpirationDate = null,
                deleteExpirationDate = null
            )
        }
    }

    fun screen2InsertActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            InsertActivityLayout(
                itemId = null,
                viewModel = null,
                prefsViewModel = null,
                addExpirationDate = null
            )
        }
    }

    fun screen3SettingsActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            SettingsActivityLayout(
                prefsViewModel = null
            )
        }
    }

    fun screen4InfoActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            InfoActivityLayout(
                prefsViewModel = null
            )
        }
    }

}