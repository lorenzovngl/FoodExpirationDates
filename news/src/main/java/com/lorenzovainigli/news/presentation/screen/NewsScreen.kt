package com.lorenzovainigli.news.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.news.R
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.presentation.component.NewsCard
import com.lorenzovainigli.news.presentation.state.NewsUiState
import java.time.Instant

@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onNewsClick: (NewsItem) -> Unit,
    onMarkAsRead: (NewsItem) -> Unit,
    onMarkAsUnread: (NewsItem) -> Unit,
    onMarkAllAsRead: () -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.news.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Nessuna novità disponibile.")
            }
        }

        else -> {
            Column {
                Row(
                    modifier = modifier.fillMaxWidth(),
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
                LazyColumn(
                    modifier = modifier.fillMaxSize()
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
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenLoadingPreview() {
    NewsScreen(
        uiState = NewsUiState(
            isLoading = true
        ),
        onNewsClick = {},
        onMarkAllAsRead = {},
        onMarkAsRead = {},
        onMarkAsUnread = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenEmptyPreview() {
    NewsScreen(
        uiState = NewsUiState(
            isLoading = false,
            news = emptyList()
        ),
        onNewsClick = {},
        onMarkAllAsRead = {},
        onMarkAsRead = {},
        onMarkAsUnread = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenContentPreview() {
    NewsScreen(
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