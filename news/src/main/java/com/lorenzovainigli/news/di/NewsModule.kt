package com.lorenzovainigli.news.di

import android.content.Context
import androidx.room.Room
import com.lorenzovainigli.news.data.local.dao.NewsDao
import com.lorenzovainigli.news.data.local.database.NewsDatabase
import com.lorenzovainigli.news.data.remote.NewsRemoteConfig
import com.lorenzovainigli.news.data.remote.datasource.NewsRemoteDataSource
import com.lorenzovainigli.news.data.remote.datasource.RssNewsRemoteDataSource
import com.lorenzovainigli.news.data.remote.parser.RssNewsParser
import com.lorenzovainigli.news.data.repository.NewsRepositoryImpl
import com.lorenzovainigli.news.domain.repository.NewsRepository
import com.lorenzovainigli.news.R
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NewsModule {

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    @Singleton
    abstract fun bindNewsRemoteDataSource(
        impl: RssNewsRemoteDataSource
    ): NewsRemoteDataSource

    companion object {

        @Provides
        @Singleton
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ): NewsDatabase {
            return Room.databaseBuilder(
                context,
                NewsDatabase::class.java,
                "news.db"
            ).build()
        }

        @Provides
        fun provideNewsDao(
            database: NewsDatabase
        ): NewsDao {
            return database.newsDao()
        }

        @Provides
        @Singleton
        fun provideRssNewsParser(): RssNewsParser {
            return RssNewsParser()
        }

        @Provides
        @Singleton
        fun provideNewsRemoteConfig(
            @ApplicationContext context: Context
        ): NewsRemoteConfig {
            return NewsRemoteConfig(
                rssUrl = context.getString(R.string.news_feed_url)
            )
        }
    }
}