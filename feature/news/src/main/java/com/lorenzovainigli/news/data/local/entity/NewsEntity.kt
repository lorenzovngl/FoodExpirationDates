package com.lorenzovainigli.news.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String?,
    val url: String,
    val publishedAtEpochMillis: Long?,
    val isRead: Boolean = false
)