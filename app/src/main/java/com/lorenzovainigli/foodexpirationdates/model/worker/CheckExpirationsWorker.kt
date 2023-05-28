package com.lorenzovainigli.foodexpirationdates.model.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.view.MainActivity

class CheckExpirationsWorker(
    appContext: Context, params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val message = inputData.getString("message")
        if (message != null) {
            showNotification(
                title = applicationContext.getString(R.string.your_food_is_expiring),
                message = message
            )
        }
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED) {
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            val notification = NotificationCompat.Builder(applicationContext, "channel_reminders")
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.fed_icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()
            notificationManager.notify(1, notification)
        }
    }

    companion object {
        const val workerID = "DailyExpirationCheck"
    }

}