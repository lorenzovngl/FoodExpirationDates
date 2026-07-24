package com.lorenzovainigli.news.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lorenzovainigli.news.domain.usecase.RefreshNewsUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshNewsWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val refreshNewsUseCase: RefreshNewsUseCase
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return refreshNewsUseCase()
            .fold(
                onSuccess = {
                    Result.success()
                },
                onFailure = {
                    Result.retry()
                }
            )
    }

    companion object {
        const val WORKER_ID = "RefreshNewsWorker"
    }
}