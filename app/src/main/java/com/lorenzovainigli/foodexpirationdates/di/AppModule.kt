package com.lorenzovainigli.foodexpirationdates.di

import android.app.Application
import androidx.room.Room
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDatesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "database.db"
        ).allowMainThreadQueries().build()
    }

//    @Provides
//    @Singleton
//    fun expirationDatesDao(database: AppDatabase): ExpirationDatesDao = database.expirationDatesDao

    @Provides
    @Singleton
    fun provideExpirationDateRepository(database: AppDatabase): ExpirationDateRepository {
        return ExpirationDatesRepositoryImpl(database.expirationDatesDao)
    }
}