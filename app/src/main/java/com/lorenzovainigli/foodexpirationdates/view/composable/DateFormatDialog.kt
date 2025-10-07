package com.lorenzovainigli.foodexpirationdates.view.composable

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.preview.LanguagePreviews
import java.text.SimpleDateFormat
import java.util.Calendar

const val DateFormatDialog = "DateFormatDialog"

@Composable
fun DateFormatDialog(
    isDialogOpen: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onClickDate: (Context, String) -> Unit = { _, _ -> },
    currentFormat: String? = null
) {
    if (isDialogOpen) {
        val context = LocalContext.current
        val selectedFormat = currentFormat ?: PreferencesRepository.getUserDateFormat(context)
        Dialog(onDismissRequest = onDismissRequest) {
            Card(
                modifier = Modifier.testTag(DateFormatDialog),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = stringResource(id = R.string.choose_the_date_format),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        modifier = Modifier.padding(bottom = 8.dp),
                        text = stringResource(id = R.string.locale_formats),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    PreferencesRepository.getAvailLocaleDateFormats().forEach { item ->
                        DateFormatRow(
                            item = item,
                            isSelected = item == selectedFormat,
                            onDismissRequest = onDismissRequest,
                            onClick = onClickDate
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                        text = stringResource(id = R.string.other_formats),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    PreferencesRepository.getAvailOtherDateFormats().forEach { item ->
                        DateFormatRow(
                            item = item,
                            isSelected = item == selectedFormat,
                            onDismissRequest = onDismissRequest,
                            onClick = onClickDate
                        )
                    }
                }
            }
        }
    }
}

const val DateFormatRow = "DateFormatRow"

@Composable
fun DateFormatRow(
    item: String,
    isSelected: Boolean = false,
    onDismissRequest: () -> Unit,
    onClick: (Context, String) -> Unit
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat(item, context.resources.configuration.locales[0])
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else
                    MaterialTheme.colorScheme.surface
            )
            .clickable {
                onClick(context, item)
                onDismissRequest()
            }
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .testTag(DateFormatRow),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = {
                onClick(context, item)
                onDismissRequest()
            }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = sdf.format(Calendar.getInstance().time),
            style = MaterialTheme.typography.bodyLarge,
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

@LanguagePreviews
@Composable
fun DateFormatDialogPreview() {
    FoodExpirationDatesTheme {
        Surface(
            color = MaterialTheme.colorScheme.surface
        ) {
            DateFormatDialog(currentFormat = "dd/MM/yyyy")
        }
    }
}