package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.analytics.NoOpAnalyticsTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AnalyticsModule {

    @Binds
    abstract fun bindAnalyticsTracker(
        impl: NoOpAnalyticsTracker
    ): AnalyticsTracker
}