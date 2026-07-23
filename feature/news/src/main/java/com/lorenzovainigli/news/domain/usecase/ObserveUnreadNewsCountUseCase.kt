package com.lorenzovainigli.news.domain.usecase

import com.lorenzovainigli.news.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveUnreadNewsCountUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<Int> {
        return newsRepository.observeUnreadCount()
    }
}