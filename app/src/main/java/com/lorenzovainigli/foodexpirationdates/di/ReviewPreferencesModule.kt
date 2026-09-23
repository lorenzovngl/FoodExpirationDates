package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.model.review.ReviewPreferences
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewPreferencesImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ReviewPreferencesModule {

    @Binds
    @Singleton
    abstract fun bindReviewPreferences(
        impl: ReviewPreferencesImpl
    ): ReviewPreferences
}