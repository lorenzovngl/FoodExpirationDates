package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.repository.NewsPreferencesRepository
import com.lorenzovainigli.news.domain.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val preferencesRepository: NewsPreferencesRepository,
) {
    suspend operator fun invoke(): Result<Unit> {
        val now = System.currentTimeMillis()
        preferencesRepository.setLastRefreshTimestamp(now)
        return newsRepository.refreshNews()
    }
}