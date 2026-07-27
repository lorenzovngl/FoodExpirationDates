package com.lorenzovainigli.news.presentation.state

import androidx.annotation.StringRes
import com.lorenzovainigli.news.domain.model.NewsItem

data class NewsUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val news: List<NewsItem> = emptyList(),
    @StringRes val errorMessageResId: Int? = null
)