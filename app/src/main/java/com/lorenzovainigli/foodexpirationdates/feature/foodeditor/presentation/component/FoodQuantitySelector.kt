package com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.R

@Composable
fun FoodQuantitySelector(
    quantity: FoodQuantity,
    onQuantityChange: (FoodQuantity) -> Unit,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false
) {
    var step by remember { mutableIntStateOf(10) }
    val hasContent = quantity.contentPerPackage != null

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.food_quantity_title),
            style = MaterialTheme.typography.headlineSmall,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text(
                    text = stringResource(R.string.food_quantity_package_count),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.food_quantity_package_count_description),
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    modifier = Modifier.focusProperties {
                        canFocus = !isPreview
                    },
                    enabled = quantity.packageCount > 1,
                    onClick = {
                        onQuantityChange(
                            quantity.copy(
                                packageCount = quantity.packageCount - 1
                            )
                        )
                    },
                ) {
                    Icon(
                        modifier = Modifier.focusProperties {
                            canFocus = !isPreview
                        },
                        imageVector = Icons.Default.Remove,
                        contentDescription = stringResource(R.string.food_quantity_decrease)
                    )
                }

                Text(
                    text = quantity.packageCount.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )

                IconButton(
                    modifier = Modifier.focusProperties {
                        canFocus = !isPreview
                    },
                    onClick = {
                        onQuantityChange(
                            quantity.copy(
                                packageCount = quantity.packageCount + 1
                            )
                        )
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.food_quantity_increase)
                    )
                }
            }
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.food_quantity_content_per_package),
                style = MaterialTheme.typography.titleMedium,
            )

            Switch(
                modifier = Modifier.focusProperties {
                    canFocus = !isPreview
                },
                checked = hasContent,
                onCheckedChange = { enabled ->
                    onQuantityChange(
                        quantity.copy(
                            contentPerPackage = if (enabled) 500 else null
                        )
                    )
                },
            )
        }

        if (hasContent) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.food_quantity_precision),
                        style = MaterialTheme.typography.labelSmall,
                    )

                    listOf(1, 10, 100).forEach { option ->
                        FilterChip(
                            modifier = Modifier.width(64.dp).focusProperties {
                                canFocus = !isPreview
                            },
                            selected = step == option,
                            onClick = { step = option },
                            label = { Text(option.toString()) }
                        )
                    }
                }

                CircularQuantityPicker(
                    value = quantity.contentPerPackage,
                    onValueChange = { newValue ->
                        onQuantityChange(
                            quantity.copy(contentPerPackage = newValue)
                        )
                    },
                    unit = quantity.unit.symbol,
                    step = step,
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = stringResource(R.string.food_quantity_unit),
                        style = MaterialTheme.typography.labelSmall,
                    )

                    QuantityUnit.entries.forEach { unit ->
                        FilterChip(
                            modifier = Modifier.width(64.dp).focusProperties {
                                canFocus = !isPreview
                            },
                            selected = quantity.unit == unit,
                            onClick = {
                                onQuantityChange(quantity.copy(unit = unit))
                            },
                            label = { Text(unit.symbol) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val legendColor =
                    MaterialTheme.colorScheme.onSurfaceVariant

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.RotateRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = legendColor,
                    )
                    Text(
                        text = stringResource(R.string.food_quantity_increase),
                        style = MaterialTheme.typography.labelSmall,
                        color = legendColor,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.RotateLeft,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = legendColor,
                    )
                    Text(
                        text = stringResource(R.string.food_quantity_decrease),
                        style = MaterialTheme.typography.labelSmall,
                        color = legendColor,
                    )
                }
            }
        }

        HorizontalDivider()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.food_quantity_total),
                style = MaterialTheme.typography.labelMedium,
            )

            Text(
                text = quantity.formatTotal(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )

            if (hasContent) {
                Text(
                    text = "${quantity.packageCount} × " +
                            "${quantity.contentPerPackage} ${quantity.unit.symbol}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun FoodQuantity.formatTotal(): String {
    val total = totalAmount
        ?: return pluralStringResource(
            R.plurals.food_quantity_packages,
            packageCount,
            packageCount
        )

    return when (unit) {
        QuantityUnit.GRAM,
        QuantityUnit.KILOGRAM -> {
            if (total >= 1000L) {
                "${formatDecimal(total)} kg"
            } else {
                "$total g"
            }
        }

        QuantityUnit.MILLILITER,
        QuantityUnit.CENTILITER,
        QuantityUnit.LITER -> {
            if (total >= 1000L) {
                "${formatDecimal(total)} L"
            } else {
                "$total ml"
            }
        }
    }
}

private fun formatDecimal(value: Long): String {
    return java.math.BigDecimal.valueOf(value, 3)
        .stripTrailingZeros()
        .toPlainString()
}


@Preview(
    showBackground = true,
    name = "Food Quantity Selector",
)
@Composable
private fun FoodQuantitySelectorPreview() {
    var quantity by remember {
        mutableStateOf(
            FoodQuantity(
                packageCount = 3,
                contentPerPackage = 500,
                unit = QuantityUnit.GRAM,
            )
        )
    }

    FoodExpirationDatesTheme {
        FoodQuantitySelector(
            quantity = quantity,
            onQuantityChange = { quantity = it },
            modifier = Modifier.padding(24.dp),
            isPreview = true
        )
    }
}
