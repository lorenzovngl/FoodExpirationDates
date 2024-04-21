package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dropdown(
    modifier: Modifier = Modifier,
    choices: Array<String>,
    onChange: (String) -> Unit,
    isExpandedStartValue: Boolean = false,
    allowUserInteraction: Boolean = true
) {
    var isExpanded by remember {
        mutableStateOf(isExpandedStartValue)
    }
    var choice by remember {
        mutableStateOf(choices[0])
    }
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = if (allowUserInteraction) it
            else false
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = choice,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            choices.forEach {
                DropdownMenuItem(
                    text = {
                        Text(it)
                    },
                    onClick = {
                        choice = it
                        onChange(it)
                        isExpanded = false
                    }
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
fun DropdownPreview() {
    FoodExpirationDatesTheme {
        Surface {
            Column {
                val choices =(0..3).map { "Choice $it" }.toTypedArray()
                Dropdown(
                    choices = choices,
                    onChange = {},
                    isExpandedStartValue = false
                )
                Dropdown(
                    choices = choices,
                    onChange = {},
                    isExpandedStartValue = true
                )
            }
        }
    }
}