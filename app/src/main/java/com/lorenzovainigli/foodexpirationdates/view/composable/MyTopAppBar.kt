package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    activity: MainActivity?,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val context = LocalContext.current
    val topBarFontState = activity?.preferencesViewModel?.getTopBarFont(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal

    LargeTopAppBar(
        title = {
            val color = MaterialTheme.colorScheme.primary
            val imageBitmap = ImageBitmap.imageResource(id = R.drawable.fed_icon)
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val monochromeIcons = PreferencesRepository.getMonochromeIcons(context)
                Canvas(
                    modifier = Modifier
                        .size(36.dp)
                        .alpha(0.8f)
                ) {
                    val imageWidth = size.width.toInt()
                    val imageHeight = size.height.toInt()
                    if (monochromeIcons) {
                        drawImage(
                            image = imageBitmap,
                            dstSize = IntSize(imageWidth, imageHeight),
                            colorFilter = ColorFilter.tint(color, BlendMode.Color)
                        )
                    }
                    drawImage(
                        image = imageBitmap,
                        dstSize = IntSize(imageWidth, imageHeight),
                        blendMode = BlendMode.DstAtop
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = when (topBarFontState) {
                        PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal -> FontWeight.Normal
                        PreferencesRepository.Companion.TopBarFont.BOLD.ordinal -> FontWeight.Medium
                        PreferencesRepository.Companion.TopBarFont.EXTRA_BOLD.ordinal -> FontWeight.Bold
                        else -> null
                    }
                )
            }
        },
        actions = actions,
        navigationIcon = navigationIcon,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(TonalElevation.level2())
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@Composable
fun MyTopAppBarPreview(){
    FoodExpirationDatesTheme(
        dynamicColor = false
    ) {
        MyTopAppBar(
            activity = null,
            title = "Lorem Ipsum",
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
    }
}