package com.lorenzovainigli.news.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lorenzovainigli.news.domain.repository.NewsPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.newsDataStore by preferencesDataStore(
    name = "news_preferences"
)

class NewsPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : NewsPreferencesRepository {

    override suspend fun getLastRefreshTimestamp(): Long {
        return context.newsDataStore.data
            .map { preferences ->
                preferences[LAST_REFRESH_TIMESTAMP_KEY] ?: 0L
            }
            .first()
    }

    override suspend fun setLastRefreshTimestamp(timestamp: Long) {
        context.newsDataStore.edit { preferences ->
            preferences[LAST_REFRESH_TIMESTAMP_KEY] = timestamp
        }
    }

    private companion object {
        val LAST_REFRESH_TIMESTAMP_KEY = longPreferencesKey("last_news_refresh_timestamp")
    }
}