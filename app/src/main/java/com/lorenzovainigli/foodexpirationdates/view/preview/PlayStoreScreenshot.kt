package com.lorenzovainigli.foodexpirationdates.view.preview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.Navigation
import com.lorenzovainigli.foodexpirationdates.view.composable.MyScaffold
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen

@Composable
fun PlayStoreScreenshot(
    text: String,
    content: @Composable () -> Unit
){
    FoodExpirationDatesTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            ScreenshotHeader(text = text)
            Image(
                modifier = Modifier
                    .padding(top = 100.dp, start = 6.dp, end = 6.dp, bottom = 6.dp)
                    .align(
                        Alignment.Center
                    ),
                painter = painterResource(id = R.drawable.pixel_3a),
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .height(810.dp)
                    .scale(.8f)
                    .absoluteOffset(x = (-3).dp, y = 90.dp)
                    .clip(RoundedCornerShape(18.dp)),
            ) {
                content()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PlayStoreScreenshotPreview() {
    PlayStoreScreenshot(
        text = stringResource(id = R.string.app_name)
    ) {
        FoodExpirationDatesTheme {
            val navController = rememberNavController()
            val showSnackbar = remember {
                mutableStateOf(false)
            }
            MyScaffold(navController = navController, showSnackbar = showSnackbar) {
                Navigation(
                    navController = navController ,
                    showSnackbar = showSnackbar,
                    startDestination = Screen.AboutScreen.route
                )
            }
        }
    }
}