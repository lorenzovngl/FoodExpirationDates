package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.repository.NewsRepository
import javax.inject.Inject

class MarkAllNewsAsReadUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() {
        newsRepository.markAllAsRead()
    }
}