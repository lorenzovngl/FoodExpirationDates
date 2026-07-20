package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.repository.NewsRepository
import javax.inject.Inject

class RefreshNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return newsRepository.refreshNews()
    }
}