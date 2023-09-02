package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.ui.theme.TonalElevation

@Composable
fun SettingsItem(
    label: String,
    content: @Composable RowScope.() -> Unit = {}
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(20))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(TonalElevation.level1()))
            .padding(12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(
            modifier = Modifier.height(8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    FoodExpirationDatesTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            tonalElevation = TonalElevation.level2()
        ) {
            SettingsItem(label = "Label") {
                Text(text = "Value")
            }
        }
    }
}