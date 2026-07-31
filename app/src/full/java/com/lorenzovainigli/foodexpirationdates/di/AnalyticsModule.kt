package com.lorenzovainigli.foodexpirationdates.di

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.analytics.FirebaseAnalyticsTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(
        @ApplicationContext context: Context
    ): FirebaseAnalytics {
         return FirebaseAnalytics.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAnalyticsTracker(
        firebaseAnalytics: FirebaseAnalytics
    ): AnalyticsTracker {
        return FirebaseAnalyticsTracker(firebaseAnalytics)
    }

}