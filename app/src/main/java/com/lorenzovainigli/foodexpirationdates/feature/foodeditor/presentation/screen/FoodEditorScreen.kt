package com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component.FoodQuantity
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component.FoodQuantitySelector
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.component.QuantityUnit
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.MeasurementUnit
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation
import com.lorenzovainigli.foodexpirationdates.view.BarcodeScannerActivity
import com.lorenzovainigli.foodexpirationdates.view.composable.Dropdown
import com.lorenzovainigli.foodexpirationdates.view.composable.MyDatePickerDialog
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

const val TEST_TAG_INSERT_ITEM = "Insert item"
const val EXTRA_PRODUCT_NAME = "product_name"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodEditorScreen(
    itemToEdit: ExpirationDate? = null,
    onSave: (ExpirationDate) -> Unit,
    onCancel: () -> Unit,
) {
    val context = LocalContext.current
    var showQuantityBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val quantityBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    var foodQuantity by remember(itemToEdit?.id) {
        mutableStateOf(
            FoodQuantity(
                packageCount = itemToEdit?.quantity ?: 1,
                contentPerPackage = itemToEdit?.contentPerPackage,
                unit = itemToEdit?.measurementUnit
                    ?.let { QuantityUnit.valueOf(it.name) }
                    ?: QuantityUnit.GRAM,
            )
        )
    }
    var foodNameToEdit = ""
    var expDate: Long? = null
    var openingDate: Long? = null
    if (itemToEdit != null) {
        foodNameToEdit = itemToEdit.foodName
        expDate = itemToEdit.expirationDate
        openingDate = itemToEdit.openingDate
    }
    var foodName by rememberSaveable {
        mutableStateOf(foodNameToEdit)
    }
    var timeSpan by remember {
        mutableIntStateOf(itemToEdit?.timeSpanDays ?: 0)
    }
    val timeSpanArray = stringArrayResource(id = R.array.time_span)
    var dropdownChoice by remember {
        mutableStateOf(timeSpanArray[0])
    }
    val datePickerExpDateState = rememberDatePickerState(expDate)
    val datePickerOpeningDateState = rememberDatePickerState(openingDate)
    var checkedOpeningDateState by remember {
        mutableStateOf(openingDate != null)
    }
    val startForResult = if (LocalContext.current is ActivityResultRegistryOwner) {
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    foodName = intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: ""
                    Log.i("Activity result", intent.getStringExtra(EXTRA_PRODUCT_NAME) ?: "")
                }
            }
        }
    } else null
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                modifier = Modifier.weight(1f),
                label = {
                    Text(
                        text = stringResource(id = R.string.food_name),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                value = foodName,
                onValueChange = { newText ->
                    foodName = newText
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )
            if (BuildConfig.FLAVOR == "full") {
                IconButton(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = {
                        startForResult?.launch(Intent(context, BarcodeScannerActivity::class.java))
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_barcode_scan),
                        contentDescription = stringResource(id = R.string.back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldDatePicker(
            datePickerState = datePickerExpDateState,
            label = stringResource(id = R.string.expiration_date)
        )
        Spacer(modifier = Modifier.height(16.dp))
        val (quantityTotal, quantityDetails) = foodQuantity.toLocalizedSummary()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showQuantityBottomSheet = true
                }
                .padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.quantity),
                style = MaterialTheme.typography.bodyLarge,
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    Text(
                        text = quantityTotal,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    if (quantityDetails != null) {
                        Text(
                            text = quantityDetails,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column (
            modifier = Modifier
                .clip(RoundedCornerShape(10))
                .background(
                    MaterialTheme.colorScheme.surfaceColorAtElevation(
                        TonalElevation.level2()
                    )
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedOpeningDateState,
                    onCheckedChange = { checkedOpeningDateState = it }
                )
                Text(
                    text = "${stringResource(
                        id = R.string.opening_date
                    )} (${stringResource(
                        id = R.string.optional
                    )})",
                )
            }
            TextFieldDatePicker(
                modifier = Modifier
                    .clickable(
                        enabled = checkedOpeningDateState,
                        onClick = {}
                    )
                    .alpha(if (checkedOpeningDateState) 1f else 0.5f),
                datePickerState = datePickerOpeningDateState,
                label = stringResource(id = R.string.opening_date),
                allowUserInteraction = checkedOpeningDateState
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .alpha(if (checkedOpeningDateState) 1f else 0.5f)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Text(
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp),
                    text = stringResource(id = R.string.time_span),
                    style = MaterialTheme.typography.bodySmall
                )
                Row {
                    TextField(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        value = timeSpan.toString(),
                        readOnly = !checkedOpeningDateState,
                        onValueChange = {
                            timeSpan = try {
                                it.toInt()
                            } catch (_: Exception) {
                                0
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Dropdown(
                        modifier = Modifier.fillMaxWidth(),
                        choices = timeSpanArray,
                        onChange = {
                            dropdownChoice = it
                        },
                        allowUserInteraction = checkedOpeningDateState
                    )
                }
            }
        }
        Row {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(top = 8.dp, end = 4.dp),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.tertiary
                ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.tertiary
                    ),
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
            Button(
                modifier = Modifier
                    .testTag(TEST_TAG_INSERT_ITEM)
                    .weight(0.5f)
                    .padding(top = 8.dp, start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary
                    ),
                onClick = {
                    try {
                        if (foodName.isNotEmpty()) {
                            if (datePickerExpDateState.selectedDateMillis != null) {
                                val entry = ExpirationDate(
                                    id = itemToEdit?.id ?: 0,
                                    foodName = foodName,
                                    expirationDate = datePickerExpDateState.selectedDateMillis!!,
                                    openingDate = if (checkedOpeningDateState) datePickerOpeningDateState.selectedDateMillis else null,
                                    timeSpanDays = if (checkedOpeningDateState) {
                                        timeSpan.let {
                                            if (dropdownChoice == timeSpanArray[0]) { // Days
                                                timeSpan
                                            } else { // Months
                                                timeSpan * 30
                                            }
                                        }
                                    } else null,
                                    quantity = foodQuantity.packageCount,
                                    contentPerPackage = foodQuantity.contentPerPackage,
                                    measurementUnit = foodQuantity.contentPerPackage?.let {
                                        MeasurementUnit.valueOf(foodQuantity.unit.name)
                                    }
                                )
                                onSave(entry)
                            } else {
                                Toast.makeText(
                                    context,
                                    R.string.please_select_a_date,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                R.string.please_enter_a_food_name,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                if (itemToEdit != null)
                    Text(text = stringResource(id = R.string.update))
                else
                    Text(text = stringResource(id = R.string.insert))
            }
        }
    }
    if (showQuantityBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showQuantityBottomSheet = false
            },
            sheetState = quantityBottomSheetState,
        ) {
            FoodQuantitySelector(
                quantity = foodQuantity,
                onQuantityChange = { foodQuantity = it },
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp,
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldDatePicker(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    label: String,
    allowUserInteraction: Boolean = true
) {
    var isDatePickerDialogOpen by remember {
        mutableStateOf(false)
    }
    TextField(
        modifier = modifier.clickable(onClick = {
            if (allowUserInteraction) {
                isDatePickerDialogOpen = true
            }
        }),
        enabled = false,
        label = {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth()
            )
        },
        value = if (datePickerState.selectedDateMillis == null) "" else {
            // TODO What kind of date format is the best here?
            val dateFormat = (DateFormat.getDateInstance(
                DateFormat.MEDIUM, Locale.getDefault()
            ) as SimpleDateFormat).toLocalizedPattern()
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            sdf.format(datePickerState.selectedDateMillis)
        },
        onValueChange = {},
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            //For Icons
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )
    if (isDatePickerDialogOpen) {
        MyDatePickerDialog(
            datePickerState = datePickerState,
            onConfirm = { isDatePickerDialogOpen = false },
            onDismiss = { isDatePickerDialogOpen = false }
        )
    }
}

@Composable
fun FoodQuantity.toLocalizedSummary(): Pair<String, String?> {
    val packagesText = pluralStringResource(
        R.plurals.food_quantity_packages,
        packageCount,
        packageCount
    )

    return toSummary { packagesText }
}

fun FoodQuantity.toSummary(formatPackages: (Int) -> String): Pair<String, String?> {
    val content = contentPerPackage
        ?: return formatPackages(packageCount) to null

    val total = content.toLong() * packageCount * unit.factor

    val totalText = when (unit) {
        QuantityUnit.GRAM,
        QuantityUnit.KILOGRAM -> {
            if (total >= 1000L) {
                "${formatQuantity(total)} kg"
            } else {
                "$total g"
            }
        }

        QuantityUnit.MILLILITER,
        QuantityUnit.CENTILITER,
        QuantityUnit.LITER -> {
            if (total >= 1000L) {
                "${formatQuantity(total)} L"
            } else {
                "$total ml"
            }
        }
    }

    return totalText to
            "$packageCount × $content ${unit.symbol}"
}

private fun formatQuantity(value: Long): String =
    java.math.BigDecimal.valueOf(value, 3)
        .stripTrailingZeros()
        .toPlainString()

@PreviewLightDark
@Composable
fun FoodEditorScreenPreview() {
    FoodExpirationDatesTheme {
        Surface {
            FoodEditorScreen(
                onSave = { },
                onCancel = { }
            )
        }
    }
}
