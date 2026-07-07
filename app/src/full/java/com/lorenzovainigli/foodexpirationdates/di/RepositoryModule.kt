package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.model.repository.ConnectivityRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.ConnectivityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindConnectivityRepository(
        repositoryImpl: ConnectivityRepositoryImpl
    ): ConnectivityRepository

}