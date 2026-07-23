package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.model.NewsItem
import com.lorenzovainigli.news.domain.repository.NewsRepository
import javax.inject.Inject

class GetLatestNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(): List<NewsItem> {
        return newsRepository.getLatestNews()
    }
}