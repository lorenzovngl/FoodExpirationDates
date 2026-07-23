package com.lorenzovainigli.news.data.remote.datasource

import com.lorenzovainigli.news.domain.model.NewsItem

interface NewsRemoteDataSource {
    suspend fun fetchNews(): List<NewsItem>
}