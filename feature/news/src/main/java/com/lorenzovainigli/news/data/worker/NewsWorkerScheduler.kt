package com.lorenzovainigli.news.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lorenzovainigli.news.domain.repository.NewsPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsWorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesRepository: NewsPreferencesRepository
) {

    suspend fun scheduleRefreshIfNeeded() {
        val lastRefresh = preferencesRepository.getLastRefreshTimestamp()
        val now = System.currentTimeMillis()

        val refreshInterval = TimeUnit.DAYS.toMillis(3)

        if (now - lastRefresh < refreshInterval) {
            return
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<RefreshNewsWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            RefreshNewsWorker.WORKER_ID,
            ExistingWorkPolicy.KEEP,
            request
        )
    }
}