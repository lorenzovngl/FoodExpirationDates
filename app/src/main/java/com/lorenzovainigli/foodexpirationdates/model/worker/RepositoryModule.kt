package com.lorenzovainigli.foodexpirationdates.model.worker

import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDatesRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideExpirationDateRepository(dao: ExpirationDatesDao): ExpirationDateRepository {
        return ExpirationDatesRepositoryImpl(dao)
    }
}
