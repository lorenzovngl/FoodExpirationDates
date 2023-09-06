package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.viewmodel.ExpirationDatesViewModel
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectViewModel(expirationDateViewModel: ExpirationDatesViewModel)
    fun injectDao(expirationDatesDao: ExpirationDatesDao)
}
