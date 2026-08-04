package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.util.lerp
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.ExpirationStatus

private const val MAX_EXPIRATION_DAYS = 7

private const val START_HUE = 30f
private const val END_HUE = 60f

private const val BASE_SATURATION = 0.80f
private const val SATURATION_THEME_OFFSET = 0.10f

private const val BASE_LIGHTNESS = 0.55f
private const val LIGHTNESS_THEME_OFFSET = 0.10f

private val BaseExpiredColor = Color(0xFF8C1313)

fun getExpirationColor(
    expirationStatus: ExpirationStatus,
    daysUntilExpiration: Int,
    isDarkTheme: Boolean,
): Color =
    when (expirationStatus) {
        ExpirationStatus.EXPIRED -> BaseExpiredColor.adjustForTheme(
            isDarkTheme = !isDarkTheme,
        )

        ExpirationStatus.EXPIRING_SOON -> getExpiringSoonColor(
            daysUntilExpiration = daysUntilExpiration,
            isDarkTheme = isDarkTheme,
        )

        ExpirationStatus.VALID -> Color.Transparent
    }

private fun getExpiringSoonColor(
    daysUntilExpiration: Int,
    isDarkTheme: Boolean,
): Color {
    val fraction =
        daysUntilExpiration.coerceIn(0, MAX_EXPIRATION_DAYS) /
                MAX_EXPIRATION_DAYS.toFloat()

    val hue = lerp(
        start = START_HUE,
        stop = END_HUE,
        fraction = fraction,
    )

    return Color.hsl(
        hue = hue,
        saturation = BASE_SATURATION.withThemeOffset(
            offset = SATURATION_THEME_OFFSET,
            isDarkTheme = isDarkTheme,
        ),
        lightness = BASE_LIGHTNESS.withThemeOffset(
            offset = LIGHTNESS_THEME_OFFSET,
            isDarkTheme = isDarkTheme,
        ),
        alpha = lerp(
            start = 1f,
            stop = 0f,
            fraction = fraction,
        ),
    )
}

private fun Color.adjustForTheme(
    isDarkTheme: Boolean,
): Color {
    val hsl = FloatArray(3)
    android.graphics.Color.colorToHSV(toArgb(), hsl)

    return Color.hsl(
        hue = hsl[0],
        saturation = hsl[1].withThemeOffset(
            offset = SATURATION_THEME_OFFSET,
            isDarkTheme = isDarkTheme,
        ),
        lightness = hsl[2].withThemeOffset(
            offset = LIGHTNESS_THEME_OFFSET,
            isDarkTheme = isDarkTheme,
        ),
        alpha = alpha,
    )
}

private fun Float.withThemeOffset(
    offset: Float,
    isDarkTheme: Boolean,
): Float {
    val signedOffset = if (isDarkTheme) {
        -offset
    } else {
        offset
    }

    return (this + signedOffset).coerceIn(0f, 1f)
}
