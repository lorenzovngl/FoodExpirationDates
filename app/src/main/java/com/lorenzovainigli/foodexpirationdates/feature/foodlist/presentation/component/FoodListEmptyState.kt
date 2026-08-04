package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

@Composable
fun FoodListEmptyState() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.no_items_found),
                style = MaterialTheme.typography.displaySmall,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.please_insert_one),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@PreviewLightDark
fun EmptyListPreview() {
    FoodExpirationDatesTheme {
        Surface {
            FoodListEmptyState()
        }
    }
}
