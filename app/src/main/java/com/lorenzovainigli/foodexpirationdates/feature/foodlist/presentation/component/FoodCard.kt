package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.ExpirationStatus
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.util.getExpirationColor
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.LocalSpacing
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.preview.getItemsForPreview

const val FOOD_CARD = "FoodCard"
const val TEST_TAG_DELETE_ITEM = "Delete item"

@Composable
fun FoodCard(
    item: FoodCardUiModel,
    onClickEdit: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val expiration = item.expirationText
    val backgroundColor = getExpirationColor(
        expirationStatus = item.expirationStatus,
        daysUntilExpiration = item.daysUntilExpiration,
        isDarkTheme = isSystemInDarkTheme(),
    )
    val foodNameTextColor = MaterialTheme.colorScheme.onSurface
    val daysRemainingTextColor = when (item.expirationStatus) {
        ExpirationStatus.EXPIRED, ExpirationStatus.EXPIRES_TODAY -> Color.White.copy(alpha = .9f)
        else -> foodNameTextColor
    }
    Surface(
        modifier = modifier
            .testTag(FOOD_CARD)
            .padding(4.dp)
            .clip(RoundedCornerShape(10.dp)),
        tonalElevation = TonalElevation.level1()
    ) {
        val colorStops = arrayOf(
            0f to Color.Transparent,
            1f to backgroundColor
        )
        val contentPadding = if (item.isOpened) {
            PaddingValues(
                horizontal = spacing.small,
                vertical = spacing.extraSmall / 2,
            )
        } else {
            PaddingValues(spacing.small)
        }
        val rowModifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colorStops = colorStops
                )
            )
            .padding(contentPadding)
        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(spacing.small)
                    .clickable(onClick = onClickEdit)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box (
                        modifier = Modifier.size(22.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, foodNameTextColor, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = item.quantity.toString(),
                            fontSize = 12.sp,
                            lineHeight = 1.sp,
                            color = foodNameTextColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.foodName,
                        color = foodNameTextColor,
                        fontSize = 18.sp
                    )
                }
                if (item.isOpened){
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
                    .padding(start = spacing.small)
                    .size(32.dp),
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

@PreviewLightDark
@Composable
fun FoodCardPreview() {
    val context = LocalContext.current
    val items = getItemsForPreview(context)
    FoodExpirationDatesTheme {
        Column {
            items.forEach { item ->
                FoodCard(
                    item = item,
                    onClickEdit = {},
                    onClickDelete = {}
                )
            }
        }
    }
}