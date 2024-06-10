package com.lorenzovainigli.foodexpirationdates.view.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.foodexpirationdates.R

@Composable
fun ScreenshotHeader(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val fontSize = text.length.let {
            when {
                it > 50 -> 26.sp
                it > 30 -> 32.sp
                else -> 36.sp
            }
        }
        Text(
            modifier = Modifier.padding(6.dp),
            text = text,
            style = MaterialTheme.typography.headlineLarge,
            fontSize = fontSize,
            fontWeight = FontWeight(200),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@LanguagePreviews
@Composable
fun ScreenshotHeaderPreview() {
    Column {
        stringArrayResource(id = R.array.screenshot_titles).forEach {
            Text(text = "Length = ${it.length}")
            ScreenshotHeader(
                text = "$it."
            )
        }
    }
}
