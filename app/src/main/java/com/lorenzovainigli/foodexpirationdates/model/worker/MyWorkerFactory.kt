package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject

class MyWorkerFactory @Inject constructor(private val repository: ExpirationDateRepository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = CheckExpirationsWorker(appContext, workerParameters, repository)
}