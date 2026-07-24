package com.lorenzovainigli.news.domain.repository

interface NewsPreferencesRepository {
    suspend fun getLastRefreshTimestamp(): Long
    suspend fun setLastRefreshTimestamp(timestamp: Long)
}