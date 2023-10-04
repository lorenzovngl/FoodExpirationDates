package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.App
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDatesRepositoryImpl

class ExpiryNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository = (context.applicationContext as App).repositoryComponent
        .provideExpirationDateRepository()
    override suspend fun doWork(): Result {
        val currentTimeMillis = System.currentTimeMillis()
        val nextDayMillis = currentTimeMillis + (24 * 60 * 60 * 1000)

        // Use the repository to get a Flow of expiring items
        val expiringItemsFlow = repository.getItemsExpiringWithinOneDay(
            currentTimeMillis, nextDayMillis
        )

        // Collect the items and send notifications
        expiringItemsFlow.collect {items : List<ExpirationDate> ->
            for (item in items) {
                val title = "Food Expiry Reminder"
                val message = "Don't forget to consume ${item.foodName} before it expires!"
                NotificationManager.sendNotification(applicationContext, title, message)
            }
        }

        return Result.success()
    }
}
