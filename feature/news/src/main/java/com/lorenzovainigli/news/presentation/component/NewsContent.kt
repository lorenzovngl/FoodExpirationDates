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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.news.R
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.presentation.state.NewsUiState

@Composable
fun NewsContent(
    uiState: NewsUiState,
    onNewsClick: (NewsItem) -> Unit,
    onMarkAsRead: (NewsItem) -> Unit,
    onMarkAsUnread: (NewsItem) -> Unit,
    onMarkAllAsRead: () -> Unit
) {
    Column {
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