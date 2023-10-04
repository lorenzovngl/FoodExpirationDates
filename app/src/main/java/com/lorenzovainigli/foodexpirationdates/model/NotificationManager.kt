package com.lorenzovainigli.foodexpirationdates.model

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.datatransport.BuildConfig

import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.worker.ExpiryNotificationWorker
import java.util.concurrent.TimeUnit

class NotificationManager(
    private val repository: ExpirationDateRepository, // Inject your repository here
    private val context: Context) {

    companion object {

        private const val channelId = "channel_reminders"
        private const val channelName = "Reminders"
        private const val notificationTag = "food_expiry_notification"
        private var permissionGranted = false

        fun setupNotificationChannel(activity: ComponentActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
                )
                val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionGranted = activity.let {
                    ContextCompat.checkSelfPermission(
                        it.applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                } == PackageManager.PERMISSION_GRANTED
            }
            val requestPermissionLauncher =
                activity.registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (BuildConfig.DEBUG) {
                        if (isGranted) {
                            Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(activity, "Permission not granted", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    permissionGranted = isGranted
                }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        @SuppressLint("MissingPermission")
        fun sendNotification(context: Context, title: String, message: String) {
            if (permissionGranted) {
                val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(androidx.core.R.drawable.notification_bg)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setGroup(notificationTag) // For grouping notifications

                val notificationManagerCompat = NotificationManagerCompat.from(context)
                notificationManagerCompat.notify(notificationTag, 0, notificationBuilder.build())


            }
        }
        fun initializeWorkManager(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicRequest = PeriodicWorkRequest.Builder(
                ExpiryNotificationWorker::class.java,
                1, // Interval in days (adjust as needed)
                TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "expiry_notification_work",
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicRequest
            )
        }
    }
    suspend fun sendExpiryNotifications() {
        val currentTimeMillis = System.currentTimeMillis()
        val nextDayMillis = currentTimeMillis + (24 * 60 * 60 * 1000)

        // Get items expiring within 1 day
        repository.getItemsExpiringWithinOneDay(currentTimeMillis, nextDayMillis).collect { items ->
            for (item in items) {
                val title = "Food Expiry Reminder"
                val message = "Don't forget to consume ${item.foodName} before it expires!"
                sendNotification(context, title, message)
            }
        }
    }

}