package screenshot

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import app.cash.paparazzi.Paparazzi
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.SettingsScreen

open class Screenshot {

    fun screen1MainActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            FoodExpirationDatesTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val showSnackbar = remember {
                    mutableStateOf(false)
                }
                MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                    MainScreen(navController = navController)
                }
            }
        }
    }

    fun screen2InsertActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            FoodExpirationDatesTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val showSnackbar = remember {
                    mutableStateOf(false)
                }
                MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                    InsertScreen(navController = navController)
                }
            }
        }
    }

    fun screen3SettingsActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            FoodExpirationDatesTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val showSnackbar = remember {
                    mutableStateOf(false)
                }
                MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                    SettingsScreen()
                }
            }
        }
    }

    fun screen4InfoActivity(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            FoodExpirationDatesTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val showSnackbar = remember {
                    mutableStateOf(false)
                }
                MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                    InfoScreen()
                }
            }
        }
    }

}