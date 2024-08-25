package screenshot

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import app.cash.paparazzi.Paparazzi
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsJsonResponse
import com.lorenzovainigli.foodexpirationdates.model.Product
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.BarcodeScannerResult
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.ContributorsList
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InfoScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.InsertScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.MainScreen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.SettingsScreen
import com.lorenzovainigli.foodexpirationdates.view.preview.PlayStoreScreenshot
import com.lorenzovainigli.foodexpirationdates.viewmodel.APIServiceViewModel.BarcodeScannerState

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
                    when (navDestination) {
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
        paparazzi.snapshot {
            Screenshot(
                header = stringArrayResource(id = R.array.screenshot_titles)[5],
                dynamicColors = true,
                navDestination = Screen.MainScreen.route
            )
        }
    }

    fun screen7BarcodeScanner(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            PlayStoreScreenshot(
                text = stringResource(id = R.string.screenshot_titles_barcode_scanner)
            ) {
                FoodExpirationDatesTheme(
                    darkTheme = false
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.barcode),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            val detectedProduct = OpenFoodFactsJsonResponse(
                                code = "",
                                product = Product(
                                    brands = "Coop",
                                    productName = "Arancia rossa",
                                    imageThumbUrl = "https://images.openfoodfacts.org/images/products/800/112/074/2346/front_it.3.100.jpg"
                                ),
                                status = 1
                            )
                            BarcodeScannerResult(
                                activity = null,
                                state = BarcodeScannerState.GOT_RESULT,
                                responseOk = true,
                                detectedProduct = detectedProduct
                            )
                        }
                    }
                }
            }
        }
    }

    fun screen8MadeWithHeart(paparazzi: Paparazzi) {
        paparazzi.snapshot {
            PlayStoreScreenshot(
                text = stringResource(id = R.string.screenshot_titles_made_with_heart)
            ) {
                FoodExpirationDatesTheme(
                    darkTheme = false
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            ContributorsList(
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

}