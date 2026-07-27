package com.lorenzovainigli.news.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorenzovainigli.news.R
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.presentation.state.NewsUiState
import java.time.Instant

@Composable
fun NewsContent(
    uiState: NewsUiState,
    onNewsClick: (NewsItem) -> Unit,
    onMarkAsRead: (NewsItem) -> Unit,
    onMarkAsUnread: (NewsItem) -> Unit,
    onMarkAllAsRead: () -> Unit
) {
    Column {
        Column {
            val notice = stringResource(R.string.news_language_notice)
            if (notice.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(4.dp)
                        .alpha(0.5f),
                    text = notice,
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                AssistChip(
                    onClick = {
                        onMarkAllAsRead()
                    },
                    label = {
                        Text(stringResource(R.string.mark_all_as_read))
                    },
                    enabled = uiState.news.any { !it.isRead }
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(uiState.news) { news ->
                NewsCard(
                    news = news,
                    onClick = { onNewsClick(news) },
                    onMarkAsRead = { onMarkAsRead(news) },
                    onMarkAsUnread = { onMarkAsUnread(news) },
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun NewsScreenContentPreview() {
    NewsContent (
        uiState = NewsUiState(
            isLoading = false,
            news = listOf(
                NewsItem(
                    id = "1",
                    title = "Food Expiration Dates 2.9.0",
                    description = "Promemoria anticipati, miglioramenti alle notifiche e correzioni minori.",
                    url = "https://example.com/news/2-9-0",
                    publishedAt = Instant.now(),
                    isRead = false
                ),
                NewsItem(
                    id = "2",
                    title = "Food Expiration Dates 2.8.0",
                    description = "Nuove funzionalità e miglioramenti alla gestione degli alimenti.",
                    url = "https://example.com/news/2-8-0",
                    publishedAt = Instant.now(),
                    isRead = true
                )
            )
        ),
        onNewsClick = {},
        onMarkAllAsRead = {},
        onMarkAsRead = {},
        onMarkAsUnread = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenContentPreviewEn() {
    NewsScreenContentPreview()
}

@Preview(showBackground = true, locale = "ja")
@Composable
private fun NewsScreenContentPreviewJa() {
    NewsScreenContentPreview()
}