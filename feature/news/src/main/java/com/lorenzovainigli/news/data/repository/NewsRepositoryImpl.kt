package com.lorenzovainigli.news.data.repository

import com.lorenzovainigli.news.data.local.dao.NewsDao
import com.lorenzovainigli.news.data.mapper.toDomain
import com.lorenzovainigli.news.data.mapper.toEntity
import com.lorenzovainigli.news.data.remote.datasource.NewsRemoteDataSource
import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val remoteDataSource: NewsRemoteDataSource
) : NewsRepository {

    override suspend fun getLatestNews(): List<NewsItem> {
        return newsDao.getNews().map { it.toDomain() }
    }

    override suspend fun refreshNews(): Result<Unit> {
        return runCatching {
            val remoteNews = remoteDataSource.fetchNews()
            val existingNews = newsDao.getNews()
            val readIds = existingNews
                .filter { it.isRead }
                .map { it.id }
                .toSet()

            newsDao.insertNews(
                remoteNews.map { newsItem ->
                    newsItem.toEntity(
                        isRead = newsItem.id in readIds
                    )
                }
            )
        }
    }

    override suspend fun markAsRead(newsId: String) {
        newsDao.markAsRead(newsId)
    }

    override suspend fun markAsUnread(newsId: String) {
        newsDao.markAsUnread(newsId)
    }

    override suspend fun markAllAsRead() {
        newsDao.markAllAsRead()
    }

    override suspend fun getUnreadCount(): Int {
        return newsDao.getUnreadCount()
    }

    override fun observeUnreadCount(): Flow<Int> {
        return newsDao.observeUnreadCount()
    }
}