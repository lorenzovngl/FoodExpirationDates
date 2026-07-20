package com.lorenzovainigli.news.presentation.route

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lorenzovainigli.news.presentation.screen.NewsScreen
import com.lorenzovainigli.news.presentation.viewmodel.NewsViewModel
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun NewsRoute(
    viewModel: NewsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NewsScreen(
        uiState = uiState,
        onNewsClick = { news ->
            viewModel.markAsRead(news.id)
            val intent = Intent(Intent.ACTION_VIEW, news.url.toUri())
            context.startActivity(intent)
        },
        onMarkAsRead =  { news ->
            viewModel.markAsRead(news.id)
        },
        onMarkAsUnread = { news ->
            viewModel.markAsUnread(news.id)
        },
        onMarkAllAsRead = {
            viewModel.markAllAsRead()
        }
    )
}