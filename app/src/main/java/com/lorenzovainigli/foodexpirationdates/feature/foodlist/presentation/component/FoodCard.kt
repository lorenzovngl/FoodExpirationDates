package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextOverflow
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
    var showActions by remember(item) {
        mutableStateOf(false)
    }
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
    Column {
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
            val modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colorStops = colorStops
                    )
                )
            ListItem(
                modifier = modifier.padding(0.dp),
                headlineContent = {
                    Text(
                        text = item.foodName,
                        color = foodNameTextColor,
                        fontSize = 18.sp
                    )
                },
                overlineContent = {
                    if (item.isOpened) {
                        Text(
                            modifier = Modifier.alpha(.8f),
                            text = stringResource(R.string.opened).uppercase()
                        )
                    }
                },
                leadingContent = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                width = 1.dp,
                                color = foodNameTextColor,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.quantity.toString(),
                            fontSize = 12.sp,
                            lineHeight = 1.sp,
                            color = foodNameTextColor.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedContent(
                            targetState = showActions,
                            transitionSpec = {
                                if (targetState) {
                                    (slideInHorizontally { it / 2 } + fadeIn()) togetherWith
                                            (slideOutHorizontally { -it / 2 } + fadeOut())
                                } else {
                                    (slideInHorizontally { -it / 2 } + fadeIn()) togetherWith
                                            (slideOutHorizontally { it / 2 } + fadeOut())
                                }.using(
                                    SizeTransform(clip = false)
                                )
                            },
                            label = "FoodCardActions"
                        ) { actionsVisible ->
                            if (actionsVisible) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        modifier = Modifier
                                            .testTag(TEST_TAG_DELETE_ITEM)
                                            .padding(start = spacing.medium)
                                            .size(36.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = {
                                            showActions = false
                                            onClickEdit()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.background,
                                            contentColor = MaterialTheme.colorScheme.secondary
                                        ),
                                        shape = CircleShape
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Edit,
                                            contentDescription = null
                                        )
                                    }

                                    Button(
                                        modifier = Modifier
                                            .testTag(TEST_TAG_DELETE_ITEM)
                                            .padding(start = spacing.medium)
                                            .size(36.dp),
                                        contentPadding = PaddingValues(0.dp),
                                        onClick = {
                                            showActions = false
                                            onClickDelete()
                                        },
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
                            } else {
                                Text(
                                    modifier = Modifier.padding(4.dp),
                                    color = daysRemainingTextColor,
                                    text = expiration,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .testTag(TEST_TAG_DELETE_ITEM)
                                .padding(start = spacing.medium)
                                .size(32.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { showActions = !showActions },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.secondary
                            ),
                            shape = CircleShape
                        ) {
                            Icon(
                                imageVector = if (showActions) {
                                    Icons.Outlined.Close
                                } else {
                                    Icons.Outlined.MoreHoriz
                                },
                                contentDescription = if (showActions) {
                                    "Close actions"
                                } else {
                                    "More options"
                                }
                            )
                        }
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = Color.Transparent
                )
            )
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