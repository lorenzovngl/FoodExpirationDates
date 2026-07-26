package com.lorenzovainigli.news.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lorenzovainigli.news.data.local.database.NewsDatabase
import com.lorenzovainigli.news.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsDaoTest {

    private lateinit var database: NewsDatabase
    private lateinit var newsDao: NewsDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NewsDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        newsDao = database.newsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertNewsAndGetNews_returnsItemsOrderedByPublicationDate() = runBlocking {
        // Given
        val olderNews = NewsEntity(
            id = "1",
            title = "Older article",
            description = null,
            url = "https://example.com/1",
            publishedAtEpochMillis = 1_000L,
            isRead = false
        )

        val newerNews = NewsEntity(
            id = "2",
            title = "Newer article",
            description = null,
            url = "https://example.com/2",
            publishedAtEpochMillis = 2_000L,
            isRead = false
        )

        // When
        newsDao.insertNews(
            listOf(
                olderNews,
                newerNews
            )
        )

        val result = newsDao.getNews()

        // Then
        assertEquals(
            listOf(
                newerNews,
                olderNews
            ),
            result
        )
    }

    @Test
    fun insertNews_withSameId_replacesExistingItem() = runBlocking {
        // Given
        val originalNews = NewsEntity(
            id = "1",
            title = "Original title",
            description = "Original description",
            url = "https://example.com/original",
            publishedAtEpochMillis = 1_000L,
            isRead = false
        )

        val updatedNews = NewsEntity(
            id = "1",
            title = "Updated title",
            description = "Updated description",
            url = "https://example.com/updated",
            publishedAtEpochMillis = 2_000L,
            isRead = true
        )

        newsDao.insertNews(listOf(originalNews))

        // When
        newsDao.insertNews(listOf(updatedNews))

        // Then
        val result = newsDao.getNews()

        assertEquals(1, result.size)
        assertEquals(updatedNews, result.first())
    }

    @Test
    fun markAsRead_updatesSpecifiedItem() = runBlocking {
        // Given
        val firstNews = NewsEntity(
            id = "1",
            title = "First article",
            description = null,
            url = "https://example.com/1",
            publishedAtEpochMillis = 1_000L,
            isRead = false
        )

        val secondNews = NewsEntity(
            id = "2",
            title = "Second article",
            description = null,
            url = "https://example.com/2",
            publishedAtEpochMillis = 2_000L,
            isRead = false
        )

        newsDao.insertNews(
            listOf(
                firstNews,
                secondNews
            )
        )

        // When
        newsDao.markAsRead("1")

        // Then
        val result = newsDao.getNews()

        val updatedFirstNews = result.first { it.id == "1" }
        val unchangedSecondNews = result.first { it.id == "2" }

        assertTrue(updatedFirstNews.isRead)
        assertFalse(unchangedSecondNews.isRead)
    }

    @Test
    fun markAsUnread_updatesSpecifiedItem() = runBlocking {
        // Given
        val firstNews = NewsEntity(
            id = "1",
            title = "First article",
            description = null,
            url = "https://example.com/1",
            publishedAtEpochMillis = 1_000L,
            isRead = true
        )

        val secondNews = NewsEntity(
            id = "2",
            title = "Second article",
            description = null,
            url = "https://example.com/2",
            publishedAtEpochMillis = 2_000L,
            isRead = true
        )

        newsDao.insertNews(
            listOf(
                firstNews,
                secondNews
            )
        )

        // When
        newsDao.markAsUnread("1")

        // Then
        val result = newsDao.getNews()

        val updatedFirstNews = result.first { it.id == "1" }
        val unchangedSecondNews = result.first { it.id == "2" }

        assertFalse(updatedFirstNews.isRead)
        assertTrue(unchangedSecondNews.isRead)
    }

    @Test
    fun markAllAsRead_updatesAllItems() = runBlocking {
        // Given
        val news = listOf(
            NewsEntity(
                id = "1",
                title = "First article",
                description = null,
                url = "https://example.com/1",
                publishedAtEpochMillis = 1_000L,
                isRead = false
            ),
            NewsEntity(
                id = "2",
                title = "Second article",
                description = null,
                url = "https://example.com/2",
                publishedAtEpochMillis = 2_000L,
                isRead = true
            ),
            NewsEntity(
                id = "3",
                title = "Third article",
                description = null,
                url = "https://example.com/3",
                publishedAtEpochMillis = 3_000L,
                isRead = false
            )
        )

        newsDao.insertNews(news)

        // When
        newsDao.markAllAsRead()

        // Then
        val result = newsDao.getNews()

        assertTrue(result.all { it.isRead })
    }

    @Test
    fun getUnreadCount_returnsUnreadItemsCount() = runBlocking {
        // Given
        val news = listOf(
            NewsEntity(
                id = "1",
                title = "First article",
                description = null,
                url = "https://example.com/1",
                publishedAtEpochMillis = 1_000L,
                isRead = false
            ),
            NewsEntity(
                id = "2",
                title = "Second article",
                description = null,
                url = "https://example.com/2",
                publishedAtEpochMillis = 2_000L,
                isRead = true
            ),
            NewsEntity(
                id = "3",
                title = "Third article",
                description = null,
                url = "https://example.com/3",
                publishedAtEpochMillis = 3_000L,
                isRead = false
            )
        )

        newsDao.insertNews(news)

        // When
        val result = newsDao.getUnreadCount()

        // Then
        assertEquals(2, result)
    }

    @Test
    fun observeNews_emitsUpdatedList() = runBlocking {
        // Given
        val news = NewsEntity(
            id = "1",
            title = "Article",
            description = null,
            url = "https://example.com/1",
            publishedAtEpochMillis = 1_000L,
            isRead = false
        )

        // When
        newsDao.insertNews(listOf(news))

        val result = newsDao.observeNews().first()

        // Then
        assertEquals(listOf(news), result)
    }

    @Test
    fun observeUnreadCount_emitsUpdatedCount() = runBlocking {
        // Given
        val news = listOf(
            NewsEntity(
                id = "1",
                title = "First article",
                description = null,
                url = "https://example.com/1",
                publishedAtEpochMillis = 1_000L,
                isRead = false
            ),
            NewsEntity(
                id = "2",
                title = "Second article",
                description = null,
                url = "https://example.com/2",
                publishedAtEpochMillis = 2_000L,
                isRead = true
            ),
            NewsEntity(
                id = "3",
                title = "Third article",
                description = null,
                url = "https://example.com/3",
                publishedAtEpochMillis = 3_000L,
                isRead = false
            )
        )

        // When
        newsDao.insertNews(news)

        val result = newsDao.observeUnreadCount().first()

        // Then
        assertEquals(2, result)
    }
}