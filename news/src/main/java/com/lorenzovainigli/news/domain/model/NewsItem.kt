package com.lorenzovainigli.news.domain.model

import java.time.Instant

data class NewsItem(
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val publishedAt: Instant?,
    val isRead: Boolean
)