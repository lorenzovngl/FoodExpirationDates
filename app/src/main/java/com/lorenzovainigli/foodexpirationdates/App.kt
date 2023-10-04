package com.lorenzovainigli.foodexpirationdates

import android.app.Application
import androidx.work.Configuration
import com.lorenzovainigli.foodexpirationdates.model.repository.RepositoryComponent
import com.lorenzovainigli.foodexpirationdates.model.worker.MyWorkerFactory
import com.lorenzovainigli.foodexpirationdates.model.worker.RepositoryModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    lateinit var repositoryComponent: RepositoryComponent



    @Inject
    lateinit var workerFactory: MyWorkerFactory


    override fun onCreate() {
        super.onCreate()

        repositoryComponent = DaggerRepositoryComponent.builder()
            .repositoryModule(RepositoryModule())
            .build()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
