package com.lorenzovainigli.foodexpirationdates.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodeditor.presentation.screen.FoodEditorScreen
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.composable.MyTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, device = "id:pixel_8")
@Composable
fun FoodEditorScreenPreview() {
    FoodExpirationDatesTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Scaffold(
                topBar = {
                    MyTopAppBar(
                        title = stringResource(id = R.string.add_item),
                        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                        actions = {},
                        navigationIcon = {
                            IconButton(
                                onClick = { }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        monochromeIcons = false
                    )
                },
                bottomBar = { }
            ) { padding ->
                Column(
                    modifier = Modifier.padding(padding)
                ) {
                    FoodEditorScreen(
                        itemToEdit = null,
                        onSave = {},
                        onCancel = {}
                    )
                }
            }
        }
    }
}