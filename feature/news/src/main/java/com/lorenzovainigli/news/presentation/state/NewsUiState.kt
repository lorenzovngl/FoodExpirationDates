package com.lorenzovainigli.news.presentation.state

import com.lorenzovainigli.news.domain.model.NewsItem

data class NewsUiState(
    val isLoading: Boolean = false,
    val news: List<NewsItem> = emptyList(),
    val errorMessage: String? = null
)