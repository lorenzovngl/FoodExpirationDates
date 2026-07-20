package com.lorenzovainigli.news.data.remote.datasource

import com.lorenzovainigli.news.data.remote.NewsRemoteConfig
import com.lorenzovainigli.news.data.remote.parser.RssNewsParser
import com.lorenzovainigli.news.domain.model.NewsItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject

class RssNewsRemoteDataSource @Inject constructor(
    private val config: NewsRemoteConfig,
    private val parser: RssNewsParser
) : NewsRemoteDataSource {

    override suspend fun fetchNews(): List<NewsItem> = withContext(Dispatchers.IO) {
        val connection = URL(config.rssUrl).openConnection() as HttpURLConnection

        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = 10_000
            connection.readTimeout = 10_000

            val xml = connection.inputStream.bufferedReader().use { it.readText() }

            parser.parse(xml)
        } finally {
            connection.disconnect()
        }
    }
}