package com.lorenzovainigli.news.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lorenzovainigli.news.data.local.dao.NewsDao
import com.lorenzovainigli.news.data.local.entity.NewsEntity

@Database(
    entities = [NewsEntity::class],
    version = 1,
    exportSchema = true
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}