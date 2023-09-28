package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.viewmodel.PreferencesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    navigationIcon: @Composable () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    prefsViewModel: PreferencesViewModel? = viewModel()
) {
    val context = LocalContext.current
    val topBarFontState = prefsViewModel?.getTopBarFont(context)?.collectAsState()?.value
        ?: PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal

    LargeTopAppBar(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = MaterialTheme.colorScheme.primary
            ),
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = when(topBarFontState){
                    PreferencesRepository.Companion.TopBarFont.NORMAL.ordinal -> FontWeight.Normal
                    PreferencesRepository.Companion.TopBarFont.BOLD.ordinal -> FontWeight.Medium
                    PreferencesRepository.Companion.TopBarFont.EXTRA_BOLD.ordinal-> FontWeight.Bold
                    else -> null
                }
            )
        },
        actions = actions,
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surface
        ),
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MyTopAppBarPreview(){
    FoodExpirationDatesTheme(
        dynamicColor = false
    ) {
        MyTopAppBar(
            title = "Title"
        )
        Spacer(modifier = Modifier.fillMaxHeight())
    }
}