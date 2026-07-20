package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.repository.NewsRepository
import javax.inject.Inject

class MarkNewsAsUnreadUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(newsId: String) {
        newsRepository.markAsUnread(newsId)
    }
}