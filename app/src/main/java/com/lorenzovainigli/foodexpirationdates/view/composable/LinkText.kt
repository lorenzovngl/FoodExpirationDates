package com.lorenzovainigli.foodexpirationdates.view.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LinkText(
    text: String,
    url: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    linkStyle: SpanStyle = SpanStyle(
        color = MaterialTheme.colorScheme.primary
    )
) {
    val annotatedString = buildAnnotatedString {
        withLink(
            LinkAnnotation.Url(
                url = url,
                styles = TextLinkStyles(style = linkStyle)
            )
        ) {
            append(text)
        }
    }

    BasicText(
        text = annotatedString,
        modifier = modifier,
        style = style
    )
}

@Preview(showBackground = true)
@Composable
private fun LinkTextPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LinkText(
                text = "Example Link",
                url = "https://example.com/",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

