package com.lorenzovainigli.news.data.repository

import com.lorenzovainigli.news.data.local.dao.NewsDao
import com.lorenzovainigli.news.data.local.entity.NewsEntity
import com.lorenzovainigli.news.data.remote.datasource.NewsRemoteDataSource
import com.lorenzovainigli.news.domain.model.NewsItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.time.Instant

class NewsRepositoryImplTest {

    private val newsDao = mockk<NewsDao>()
    private val remoteDataSource = mockk<NewsRemoteDataSource>()

    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setUp() {
        repository = NewsRepositoryImpl(
            newsDao = newsDao,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `refreshNews preserves read state`() = runTest {
        // Given
        val localNews = listOf(
            NewsEntity(
                id = "1",
                title = "Old",
                description = null,
                url = "url",
                publishedAtEpochMillis = 1000,
                isRead = true
            )
        )

        val remoteNews = listOf(
            NewsItem(
                id = "1",
                title = "Old",
                description = null,
                url = "url",
                publishedAt = Instant.ofEpochMilli(1000),
                isRead = false // will be ignored
            )
        )

        coEvery {
            remoteDataSource.fetchNews()
        } returns remoteNews

        coEvery {
            newsDao.getNews()
        } returns localNews

        coEvery {
            newsDao.insertNews(any())
        } just Runs

        // When
        repository.refreshNews()

        // Then
        coVerify {
            newsDao.insertNews(
                match { inserted ->

                    inserted.size == 1 &&
                            inserted.first().id == "1" &&
                            inserted.first().isRead
                }
            )
        }
    }

    @Test
    fun `refreshNews marks new articles as unread`() = runTest {
        // Given
        val localNews = listOf(
            NewsEntity(
                id = "1",
                title = "Existing article",
                description = null,
                url = "https://example.com/1",
                publishedAtEpochMillis = 1_000L,
                isRead = true
            )
        )

        val remoteNews = listOf(
            NewsItem(
                id = "1",
                title = "Existing article",
                description = null,
                url = "https://example.com/1",
                publishedAt = Instant.ofEpochMilli(1_000L),
                isRead = false
            ),
            NewsItem(
                id = "2",
                title = "New article",
                description = null,
                url = "https://example.com/2",
                publishedAt = Instant.ofEpochMilli(2_000L),
                isRead = false
            )
        )

        coEvery {
            remoteDataSource.fetchNews()
        } returns remoteNews

        coEvery {
            newsDao.getNews()
        } returns localNews

        coEvery {
            newsDao.insertNews(any())
        } just Runs

        // When
        val result = repository.refreshNews()

        // Then
        assertTrue(result.isSuccess)

        coVerify(exactly = 1) {
            newsDao.insertNews(
                match { insertedNews ->
                    val existingArticle = insertedNews.first { it.id == "1" }
                    val newArticle = insertedNews.first { it.id == "2" }

                    existingArticle.isRead &&
                            !newArticle.isRead
                }
            )
        }
    }

    @Test
    fun `refreshNews returns failure when remote fetch throws`() = runTest {
        // Given
        val exception = IOException("Network error")

        coEvery {
            remoteDataSource.fetchNews()
        } throws exception

        // When
        val result = repository.refreshNews()

        // Then
        assertTrue(result.isFailure)
        assertSame(exception, result.exceptionOrNull())

        coVerify(exactly = 0) {
            newsDao.getNews()
        }

        coVerify(exactly = 0) {
            newsDao.insertNews(any())
        }
    }

    @Test
    fun `getLatestNews returns mapped local news`() = runTest {
        // Given
        val publishedAtMillis = 1_000L

        val entities = listOf(
            NewsEntity(
                id = "1",
                title = "First article",
                description = "First description",
                url = "https://example.com/1",
                publishedAtEpochMillis = publishedAtMillis,
                isRead = true
            ),
            NewsEntity(
                id = "2",
                title = "Second article",
                description = null,
                url = "https://example.com/2",
                publishedAtEpochMillis = null,
                isRead = false
            )
        )

        coEvery {
            newsDao.getNews()
        } returns entities

        // When
        val result = repository.getLatestNews()

        // Then
        assertEquals(
            listOf(
                NewsItem(
                    id = "1",
                    title = "First article",
                    description = "First description",
                    url = "https://example.com/1",
                    publishedAt = Instant.ofEpochMilli(publishedAtMillis),
                    isRead = true
                ),
                NewsItem(
                    id = "2",
                    title = "Second article",
                    description = null,
                    url = "https://example.com/2",
                    publishedAt = null,
                    isRead = false
                )
            ),
            result
        )

        coVerify(exactly = 1) {
            newsDao.getNews()
        }
    }

    @Test
    fun `markAsRead delegates to dao`() = runTest {
        // Given
        coEvery {
            newsDao.markAsRead("news-1")
        } just Runs

        // When
        repository.markAsRead("news-1")

        // Then
        coVerify(exactly = 1) {
            newsDao.markAsRead("news-1")
        }
    }

    @Test
    fun `markAsUnread delegates to dao`() = runTest {
        // Given
        coEvery {
            newsDao.markAsUnread("news-1")
        } just Runs

        // When
        repository.markAsUnread("news-1")

        // Then
        coVerify(exactly = 1) {
            newsDao.markAsUnread("news-1")
        }
    }

    @Test
    fun `markAllAsRead delegates to dao`() = runTest {
        // Given
        coEvery {
            newsDao.markAllAsRead()
        } just Runs

        // When
        repository.markAllAsRead()

        // Then
        coVerify(exactly = 1) {
            newsDao.markAllAsRead()
        }
    }

    @Test
    fun `getUnreadCount returns dao count`() = runTest {
        // Given
        coEvery {
            newsDao.getUnreadCount()
        } returns 3

        // When
        val result = repository.getUnreadCount()

        // Then
        assertEquals(3, result)

        coVerify(exactly = 1) {
            newsDao.getUnreadCount()
        }
    }

    @Test
    fun `observeUnreadCount returns dao flow`() = runTest {
        // Given
        val unreadCountFlow = flowOf(3)

        every {
            newsDao.observeUnreadCount()
        } returns unreadCountFlow

        // When
        val result = repository.observeUnreadCount()

        // Then
        assertSame(unreadCountFlow, result)

        verify(exactly = 1) {
            newsDao.observeUnreadCount()
        }
    }
}