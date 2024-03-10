package screenshot

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.compose.rememberNavController
import app.cash.paparazzi.Paparazzi
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.SettingsScreen
import com.lorenzovainigli.foodexpirationdates.view.preview.PlayStoreScreenshot

open class Screenshot {

    @Composable
    fun Screenshot(
        header: String,
        darkMode: Boolean = false,
        dynamicColors: Boolean = false,
        navDestination: String
    ) {
        PlayStoreScreenshot(
            text = header
        ) {
            FoodExpirationDatesTheme(
                darkTheme = darkMode,
                dynamicColor = dynamicColors
            ) {
                val navController = rememberNavController()
                val showSnackbar = remember {
                    mutableStateOf(false)
                }
                MyScaffold(
                    navController = navController,
                    showSnackbar = showSnackbar,
                    navDestination = navDestination
                ) {
                    // Navigation composable not work here
                    when (navDestination){
                        Screen.AboutScreen.route -> InfoScreen()
                        Screen.InsertScreen.route -> InsertScreen(navController = navController)
                        Screen.SettingsScreen.route -> SettingsScreen()
                        else -> MainScreen(navController = navController)
                    }
                }
            }
        }
    }

    fun screen1MainActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[0],
                navDestination = Screen.MainScreen.route
            )
        }
    }

    fun screen2InsertActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[1],
                navDestination = Screen.InsertScreen.route
            )
        }
    }

    fun screen3SettingsActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[2],
                navDestination = Screen.SettingsScreen.route
            )
        }
    }

    fun screen4InfoActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[3],
                navDestination = Screen.AboutScreen.route
            )
        }
    }

    fun screen5DarkMode(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[4],
                darkMode = true,
                navDestination = Screen.MainScreen.route
            )
        }
    }

    fun screen6DynamicColors(paparazzi: Paparazzi) {
        /* TODO
            Restore after the following bug is fixed
            android.content.res.Resources$NotFoundException at ScreenshotArabic.kt:59
         */
        /*paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[5],
                dynamicColors = true,
                navDestination = Screen.MainScreen.route
            )
        }*/
    }

}