package com.lorenzovainigli.foodexpirationdates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation

const val PREVIEW_DEVICE = "spec:width=1080px,height=1920px,dpi=320"

@Composable
fun PlayStoreScreenshot(
    text: String,
    statusBarAsOverlay: Boolean = false,
    navigationBarElevation: Dp = TonalElevation.level2(),
    darkTheme: Boolean? = null,
    dynamicColors: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        GlowBackground {
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
            val scaleFactor = .855f
            Box(
                modifier = Modifier
                    .width((360 / scaleFactor).dp)
                    .height((900 / scaleFactor).dp)
                    .align(Alignment.TopCenter)
                    .absoluteOffset(x = (-3).dp, y = 46.dp)
                    .scale(scaleFactor)
                    .clip(RoundedCornerShape((36 / scaleFactor).dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                FoodExpirationDatesTheme(
                    darkTheme = darkTheme ?: isSystemInDarkTheme(),
                    dynamicColor = dynamicColors
                ) {
                    Column() {
                        Box(Modifier.weight(1f)) {
                            if (statusBarAsOverlay) {
                                Box(
                                    modifier = Modifier.background(
                                        MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    content()
                                    StatusBar(statusBarAsOverlay)
                                }
                            } else {
                                Column(
                                    modifier = Modifier.background(
                                        MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    StatusBar(statusBarAsOverlay)
                                    Box(Modifier.weight(1f)) {
                                        content()
                                    }
                                }
                            }
                        }
                        NavigationBar(navigationBarElevation)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(
    overlay: Boolean ,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (overlay) Color.Transparent
                else MaterialTheme.colorScheme.surface)
            .padding(
                top = 14.dp,
                start = 16.dp,
                bottom = 8.dp,
                end = 16.dp
            )
    ) {
        val statusBarItemsSize = 18
        val darkTheme = isSystemInDarkTheme()
        val color = if (darkTheme) Color.White else Color.Black
        Text(
            text = "15:56",
            fontSize = statusBarItemsSize.sp,
            color = color
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(statusBarItemsSize.dp),
            imageVector = Icons.Default.Wifi,
            contentDescription = null,
            tint = color
        )
        Icon(
            modifier = Modifier.size(statusBarItemsSize.dp),
            imageVector = Icons.Default.SignalCellularAlt,
            contentDescription = null,
            tint = color
        )
        Icon(
            modifier = Modifier.size(statusBarItemsSize.dp),
            imageVector = Icons.Default.Battery5Bar,
            contentDescription = null,
            tint = color
        )
    }
}

@PreviewLightDark
@Composable
fun StatusBarPreview() {
    FoodExpirationDatesTheme() {
        StatusBar(overlay = false)
    }
}

@Composable
fun NavigationBar(
    elevation: Dp
){
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .height(6.dp)
                .width(100.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.3f
                ))
        )
    }
}

@PreviewLightDark
@Composable
fun NavigationBarPreview() {
    FoodExpirationDatesTheme() {
        NavigationBar(TonalElevation.level2())
    }
}

@Preview(showBackground = true, device = PREVIEW_DEVICE)
@Composable
fun PlayStoreScreenshotPreview() {
    FoodExpirationDatesTheme {
        PlayStoreScreenshot(
            text = stringResource(id = R.string.app_name)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Content"
                )
            }
        }
    }
}

@Preview(showBackground = true, device = PREVIEW_DEVICE)
@Composable
fun PlayStoreScreenshotAsOverlayPreview() {
    FoodExpirationDatesTheme {
        PlayStoreScreenshot(
            text = stringResource(id = R.string.app_name),
            statusBarAsOverlay = true,
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Content"
                )
            }
        }
    }
}