package com.lorenzovainigli.foodexpirationdates

import android.app.Application
import androidx.work.Configuration
import com.lorenzovainigli.foodexpirationdates.model.worker.MyWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
