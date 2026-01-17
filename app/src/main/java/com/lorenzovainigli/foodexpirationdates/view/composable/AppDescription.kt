package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.foodexpirationdates.DEVELOPER_NAME
import com.lorenzovainigli.foodexpirationdates.DEVELOPER_WEBSITE
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.ui.theme.FoodExpirationDatesTheme

const val URL_TAG = "URL"

@Composable
fun AppDescription(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground
    )) {
    val uriHandler = LocalUriHandler.current
    val fullText = stringResource(R.string.app_description, stringResource(id = R.string.app_name))

    val start = fullText.indexOf(DEVELOPER_NAME)
    val end = start + DEVELOPER_NAME.length

    val annotatedText = buildAnnotatedString {
        append(fullText)

        if (start >= 0) {
            addStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary
                ),
                start = start,
                end = end
            )
            addStringAnnotation(
                tag = URL_TAG,
                annotation = DEVELOPER_WEBSITE,
                start = start,
                end = end
            )
        }
    }

    BasicText(
        modifier = modifier.pointerInput(Unit){
            detectTapGestures(
                onTap = { offset ->
                    annotatedText.getStringAnnotations(URL_TAG, start, end)
                        .firstOrNull()
                        ?.let { uriHandler.openUri(it.item) }
                }
            )
        },
        text = annotatedText,
        style = textStyle
    )
}

@Preview(showBackground = true)
@Composable
fun AboutThisAppIntroPreview() {
    FoodExpirationDatesTheme {
        AppDescription()
    }
}
