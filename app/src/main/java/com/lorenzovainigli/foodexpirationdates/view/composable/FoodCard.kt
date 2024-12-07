package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.DarkRed
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.LightRed
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.getItemsForPreview
import java.text.SimpleDateFormat
import java.util.Calendar

const val FOOD_CARD = "FoodCard"
const val TEST_TAG_DELETE_ITEM = "Delete item"

@Composable
fun FoodCard(
    item: ExpirationDate,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    isInDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val dateFormat = PreferencesRepository.getUserDateFormat(context)
    val monochromeIcons = PreferencesRepository.getMonochromeIcons(context)
    val sdf = SimpleDateFormat(dateFormat, context.resources.configuration.locales[0])
    val today = Calendar.getInstance()
    val twoDaysAgo = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -2)
    }
    val yesterday = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, -1)
    }
    val tomorrow = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 1)
    }
    val withinAWeek = Calendar.getInstance().apply {
        add(Calendar.DAY_OF_MONTH, 7)
    }
    val msInADay = (1000 * 60 * 60 * 24)
    val expirationDate = computeExpirationDate(item)
    val expiration = expirationDate.let {
        when {
            it < twoDaysAgo.time.time -> {
                val days = (today.time.time - expirationDate) / msInADay
                stringResource(R.string.n_days_ago, days)
            }

            it < yesterday.time.time -> stringResource(R.string.yesterday)
            it < today.time.time -> stringResource(R.string.today)
            it < tomorrow.time.time -> stringResource(R.string.tomorrow)
            it < withinAWeek.time.time -> {
                val days = (expirationDate - today.time.time) / msInADay
                stringResource(R.string.in_n_days, days + 1)
            }

            else -> sdf.format(expirationDate)
        }
    }
    val days = (expirationDate - today.time.time) / msInADay
    val bgColor = expirationDate.let {
        when {
            it < today.time.time -> if (isInDarkTheme) LightRed else DarkRed
            it < withinAWeek.time.time -> getColorForDays(days = days.toInt())
            else -> Color.Transparent
        }
    }
    val foodNameTextColor = MaterialTheme.colorScheme.onSurface
    val daysRemainingTextColor =
        if (expirationDate < today.time.time) Color.White.copy(alpha = .9f)
        else foodNameTextColor
    Surface(
        modifier = Modifier
            .testTag(FOOD_CARD)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp)),
        tonalElevation = TonalElevation.level1()
    ) {
        val colorStops = arrayOf(
            0f to Color.Transparent,
            1f to bgColor
        )
        var rowModifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colorStops = colorStops
                )
            )
        rowModifier = when (item.openingDate) {
            null -> rowModifier.padding(8.dp)
            else -> rowModifier.padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
        }
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val color = MaterialTheme.colorScheme.primary
            val imageBitmap =
                ImageBitmap.imageResource(id = R.drawable.icons8_cesto_96)
            Canvas(
                modifier = Modifier
                    .size(36.dp)
                    .alpha(0.8f)
            ) {
                if (monochromeIcons) {
                    drawImage(
                        image = imageBitmap,
                        colorFilter = ColorFilter.tint(color, BlendMode.Color)
                    )
                }
                drawImage(
                    image = imageBitmap,
                    blendMode = BlendMode.DstAtop
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .clickable(onClick = onClickEdit)
            ) {
                Text(
                    text = item.foodName,
                    color = foodNameTextColor,
                    fontSize = 18.sp
                )
                if (item.openingDate != null){
                    Text(
                        modifier = Modifier.alpha(.8f),
                        text = stringResource(R.string.opened),
                        fontSize = 12.sp
                    )
                }
            }
            Text(
                modifier = Modifier.padding(4.dp),
                color = daysRemainingTextColor,
                text = expiration
            )
            Button(
                modifier = Modifier
                    .testTag(TEST_TAG_DELETE_ITEM)
                    .padding(start = 8.dp)
                    .width(32.dp)
                    .height(32.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = onClickDelete,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.secondary
                ),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = null
                )
            }
        }
    }
}

fun getColorForDays(days: Int): Color {
    if (days > 7) {
        return Color.Transparent
    }
    val maxDays = 7  // Maximum number of days (e.g., 7 days for yellow)
    val hueEnd = 60f  // Hue for yellow
    val hueStart = 30f  // Hue for orange
    val fraction = days.toFloat() / maxDays
    val hue = lerp(hueStart, hueEnd, fraction)
    val alpha = lerp(1f, 0f, fraction)
    val saturation = 0.8f
    val lightness = 0.5f
    return Color.hsl(hue = hue, saturation = saturation, lightness = lightness, alpha = alpha)
}

@PreviewLightDark
@Composable
fun FoodCardPreview() {
    val items = getItemsForPreview(LocalContext.current)
    FoodExpirationDatesTheme {
        Column {
            items.forEachIndexed { index, item ->
                FoodCard(
                    item = ExpirationDate(
                        id = 0,
                        foodName = item.foodName,
                        expirationDate = item.expirationDate,
                        openingDate = if (index in listOf(2, 4)) item.expirationDate else null,
                        timeSpanDays = 0
                    ),
                    onClickEdit = {},
                    onClickDelete = {},
                    isInDarkTheme = isSystemInDarkTheme()
                )
            }
        }
    }
}