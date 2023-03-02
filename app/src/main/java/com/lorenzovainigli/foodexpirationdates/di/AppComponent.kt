package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDateViewModel
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectViewModel(expirationDateViewModel: ExpirationDateViewModel)
    fun injectDao(expirationDatesDao: ExpirationDatesDao)
}
