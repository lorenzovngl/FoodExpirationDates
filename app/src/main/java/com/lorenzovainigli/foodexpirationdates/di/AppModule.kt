package com.lorenzovainigli.foodexpirationdates.di

import android.app.Application
import androidx.room.Room
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.MIGRATION_3_4
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDatesRepositoryImpl
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
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
        ).addMigrations(MIGRATION_3_4)
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigrationOnDowngrade()
            .allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideExpirationDateRepository(database: AppDatabase): ExpirationDateRepository {
        return ExpirationDatesRepositoryImpl(database.expirationDatesDao)
    }

    @Provides
    @Singleton
    fun providePreferencesRepository(): PreferencesRepository {
        return PreferencesRepository()
    }
}