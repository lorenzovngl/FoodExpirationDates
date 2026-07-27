package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.news.data.worker.RefreshNewsWorker
import com.lorenzovainigli.news.domain.usecase.RefreshNewsUseCase
import java.time.Clock
import javax.inject.Inject

class MyWorkerFactory @Inject constructor(
    private val repository: ExpirationDateRepository,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            CheckExpirationsWorker::class.java.name -> {
                CheckExpirationsWorker(
                    appContext = appContext,
                    params = workerParameters,
                    repository = repository,
                    clock = Clock.systemDefaultZone()
                )
            }
            RefreshNewsWorker::class.java.name -> {
                RefreshNewsWorker(
                    appContext = appContext,
                    params = workerParameters,
                    refreshNewsUseCase = refreshNewsUseCase,
                )
            }
            else -> null
        }
    }
}