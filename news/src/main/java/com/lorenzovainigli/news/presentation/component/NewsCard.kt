package com.lorenzovainigli.news.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lorenzovainigli.news.R
import com.lorenzovainigli.news.domain.model.NewsItem
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsCard(
    news: NewsItem,
    onClick: () -> Unit,
    onMarkAsRead: () -> Unit,
    onMarkAsUnread: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    showMenu = true
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (!news.isRead) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = "Apre una pagina web",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            news.publishedAt?.let { publishedAt ->
                Text(
                    text = remember(publishedAt) {
                        publishedAt.formatAsLocalizedDate()
                    },
                    fontWeight = if (!news.isRead) FontWeight.Bold else FontWeight.Normal,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            news.description?.let { description ->
                Text(
                    text = description,
                    fontWeight = if (!news.isRead) FontWeight.SemiBold else FontWeight.Normal,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }



    if (showMenu) {
        ModalBottomSheet(
            onDismissRequest = {
                showMenu = false
            }
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = if (news.isRead) stringResource(R.string.mark_as_unread) else stringResource(R.string.mark_as_read)
                    )
                },
                modifier = Modifier.clickable {
                    showMenu = false
                    if (news.isRead) onMarkAsUnread() else onMarkAsRead()
                },
                colors = ListItemDefaults.colors().copy(
                    containerColor = Color.Transparent
                )
            )
        }
    }
}

private fun Instant.formatAsLocalizedDate(): String {
    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(Locale.getDefault())

    return atZone(ZoneId.systemDefault()).format(formatter)
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    Column {
        NewsCard(
            news = NewsItem(
                id = "1",
                title = "Food Expiration Dates 2.9.0",
                description = "Promemoria anticipati, miglioramenti alle notifiche e correzioni minori.",
                url = "https://example.com/news/2-9-0",
                publishedAt = Instant.now(),
                isRead = false
            ),
            onClick = {},
            onMarkAsRead = {},
            onMarkAsUnread = {}
        )
        NewsCard(
            news = NewsItem(
                id = "1",
                title = "Food Expiration Dates 2.9.0",
                description = "Promemoria anticipati, miglioramenti alle notifiche e correzioni minori.",
                url = "https://example.com/news/2-9-0",
                publishedAt = Instant.now(),
                isRead = true
            ),
            onClick = {},
            onMarkAsRead = {},
            onMarkAsUnread = {}
        )
    }
}