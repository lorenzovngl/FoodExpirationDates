package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme
import com.lorenzovainigli.foodexpirationdates.view.preview.DefaultPreviews

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = MaterialTheme.typography.labelLarge
){
    // From https://stackoverflow.com/a/66090448
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }
    val defaultFontSize = MaterialTheme.typography.labelLarge.fontSize
    Text(
        text = text,
        style = resizedTextStyle,
        softWrap = false,
        modifier = modifier.drawWithContent {
            if (shouldDraw){
                drawContent()
            }
        },
        onTextLayout = { result ->
            if (result.didOverflowWidth) {
                if (style.fontSize.isUnspecified){
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true
            }
        }
    )
}

@DefaultPreviews
@Composable
fun AutoResizedTextPreview(){
    FoodExpirationDatesTheme {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            (400 downTo 100 step 100).forEach {
                Box(
                    modifier = Modifier.width(it.dp)
                ) {
                    Column {
                       Button(onClick = {}) {
                            AutoResizedText(
                                text = "Lorem ipsum dolor sit amet",
                                style = MaterialTheme.typography.headlineLarge
                            )
                        }
                    }
                }
            }
        }
    }
}