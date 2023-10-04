package com.lorenzovainigli.foodexpirationdates.model.repository

import com.lorenzovainigli.foodexpirationdates.model.worker.RepositoryModule
import dagger.Component

@Component(modules = [RepositoryModule::class])
interface RepositoryComponent {
    fun provideExpirationDateRepository(): ExpirationDateRepository
}
