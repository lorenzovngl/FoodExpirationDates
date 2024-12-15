package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.Language
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun LanguagePickerDialog(
    isDialogOpen: Boolean = true,
    onDismiss: () -> Unit = {}
) {
    if (isDialogOpen) {
        val context = LocalContext.current
        val storedLanguage = PreferencesRepository.getLanguage(context)
        var selectedLanguage = remember {
            mutableStateOf(storedLanguage)
        }
        Dialog(
            onDismissRequest = onDismiss
        ) {
            Card(
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = stringResource(R.string.select_language),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(
                        modifier = Modifier
                            .height(480.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Language.entries.forEach { language ->
                            Row(
                                modifier = Modifier.fillMaxWidth(1f).clickable(
                                        onClick = {
                                            selectedLanguage.value = language.code
                                        }),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedLanguage.value == language.code,
                                    onClick = {
                                        selectedLanguage.value = language.code
                                    }
                                )
                                Text(
                                    text = language.label,
                                    color = if (selectedLanguage.value == language.code)
                                        MaterialTheme.colorScheme.primary
                                    else Color.Unspecified
                                )
                            }
                        }
                    }
                    Button(
                        modifier = Modifier.align(Alignment.End),
                        onClick = {
                            LocaleHelper.changeLanguage(context, selectedLanguage.value)
                            PreferencesRepository.setLanguage(
                                context,
                                language = selectedLanguage.value
                            )
                        }
                    ) {
                        Text(stringResource(R.string.apply))
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
fun LanguagePickerDialogPreview() {
    FoodExpirationDatesTheme {
        Surface {
            LanguagePickerDialog()
        }
    }
}