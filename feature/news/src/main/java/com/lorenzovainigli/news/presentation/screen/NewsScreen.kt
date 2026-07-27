package com.lorenzovainigli.news.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lorenzovainigli.news.R
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.presentation.component.NewsContent
import com.lorenzovainigli.news.presentation.state.NewsUiState
import java.time.Instant

@Composable
fun NewsScreen(
    uiState: NewsUiState,
    onNewsClick: (NewsItem) -> Unit,
    onMarkAsRead: (NewsItem) -> Unit,
    onMarkAsUnread: (NewsItem) -> Unit,
    onMarkAllAsRead: () -> Unit,
    onRefresh: () -> Unit,
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
        else -> {
            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh,
                modifier = modifier.fillMaxSize()
            ) {
                when {
                    uiState.errorMessageResId != null -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(uiState.errorMessageResId))
                        }
                    }
                    uiState.news.isEmpty() -> {
                        Box(
                            modifier = modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.news_empty))
                        }
                    }
                    else -> {
                        NewsContent(
                            uiState = uiState,
                            onNewsClick = onNewsClick,
                            onMarkAsRead = onMarkAsRead,
                            onMarkAsUnread = onMarkAsUnread,
                            onMarkAllAsRead = onMarkAllAsRead
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
        onMarkAsUnread = {},
        onRefresh = {}
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
        onMarkAsUnread = {},
        onRefresh = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun NewsScreenErrorPreview() {
    NewsScreen(
        uiState = NewsUiState(
            errorMessageResId = R.string.connection_error
        ),
        onNewsClick = {},
        onMarkAllAsRead = {},
        onMarkAsRead = {},
        onMarkAsUnread = {},
        onRefresh = {}
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
        onMarkAsUnread = {},
        onRefresh = {}
    )
}