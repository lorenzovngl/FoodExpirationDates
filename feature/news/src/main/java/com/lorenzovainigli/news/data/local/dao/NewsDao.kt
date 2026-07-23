package com.lorenzovainigli.news.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lorenzovainigli.news.data.local.entity.NewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("""
        SELECT * 
        FROM news
        ORDER BY publishedAtEpochMillis DESC
    """)
    fun observeNews(): Flow<List<NewsEntity>>

    @Query("""
        SELECT * 
        FROM news
        ORDER BY publishedAtEpochMillis DESC
    """)
    suspend fun getNews(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: List<NewsEntity>)

    @Query("""
        UPDATE news
        SET isRead = 1
        WHERE id = :newsId
    """)
    suspend fun markAsRead(newsId: String)

    @Query("""
        UPDATE news
        SET isRead = 0
        WHERE id = :newsId
    """)
    suspend fun markAsUnread(newsId: String)

    @Query("""
        UPDATE news
        SET isRead = 1
    """)
    suspend fun markAllAsRead()

    @Query("""
        SELECT COUNT(*)
        FROM news
        WHERE isRead = 0
    """)
    suspend fun getUnreadCount(): Int

    @Query("""
        SELECT COUNT(*)
        FROM news
        WHERE isRead = 0
    """)
    fun observeUnreadCount(): Flow<Int>
}