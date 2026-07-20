package com.lorenzovainigli.news.domain.repository

import com.lorenzovainigli.news.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getLatestNews(): List<NewsItem>
    suspend fun refreshNews(): Result<Unit>
    suspend fun markAsRead(newsId: String)
    suspend fun markAsUnread(newsId: String)
    suspend fun markAllAsRead()
    suspend fun getUnreadCount(): Int
    fun observeUnreadCount(): Flow<Int>
}