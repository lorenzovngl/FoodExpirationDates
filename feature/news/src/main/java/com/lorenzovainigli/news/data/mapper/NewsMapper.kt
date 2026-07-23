package com.lorenzovainigli.news.data.mapper

import com.lorenzovainigli.news.data.local.entity.NewsEntity
import com.lorenzovainigli.news.domain.model.NewsItem
import java.time.Instant

fun NewsEntity.toDomain(): NewsItem {
    return NewsItem(
        id = id,
        title = title,
        description = description,
        url = url,
        publishedAt = publishedAtEpochMillis?.let(Instant::ofEpochMilli),
        isRead = isRead
    )
}

fun NewsItem.toEntity(isRead: Boolean = this.isRead): NewsEntity {
    return NewsEntity(
        id = id,
        title = title,
        description = description,
        url = url,
        publishedAtEpochMillis = publishedAt?.toEpochMilli(),
        isRead = isRead
    )
}