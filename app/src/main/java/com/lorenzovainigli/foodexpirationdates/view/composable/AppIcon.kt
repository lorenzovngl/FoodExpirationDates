package com.lorenzovainigli.foodexpirationdates.view.composable

import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeColorFilter
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun AppIcon(
    size: Dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val aspectRatio: Float = 1f / 48f * 44f
    val resizeRatio: Float = size / 48.dp           // 1 : x = 48 : size
    val scaleRatio: Float = 0.28f * size / 48.dp    // 0.28 : x = 48 : size
    val width: Dp = size
    val height: Dp = size * aspectRatio
    // Rows 1-4: RGBA channels of the output color
    // Columns 1-4: RGBA channels of the input color
    val matrix = floatArrayOf(
        color.red, 0f, 0f, 0f, 0f,
        color.green, 0f, 0f, 0f, 0f,
        color.blue, 0f, 0f, 0f, 0f,
        1f, 0f, 0f, 0f, 0f
    )
    val imageBitmap =
        ImageBitmap.imageResource(id = R.drawable.fed_icon)
    Canvas(
        modifier = Modifier
            .padding(horizontal = 7.dp)
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(12.dp * resizeRatio))
    ) {
        scale(
            scaleX = scaleRatio,
            scaleY = scaleRatio,
            pivot = Offset(0f, 0f)
        ) {
            drawImage(
                image = imageBitmap,
                topLeft = Offset(
                    x = -24f * resizeRatio,
                    y = -20f * resizeRatio
                ),
                colorFilter = ColorMatrixColorFilter(matrix).asComposeColorFilter()
            )
        }
    }
}

@Preview
@Composable
fun AppIconPreview() {
    FoodExpirationDatesTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                AppIcon(size = 48.dp)
            }
        }
    }
}