package com.lorenzovainigli.foodexpirationdates.model

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.model.worker.CheckExpirationsWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class NotificationManager {

    companion object {

        const val CHANNEL_REMINDERS_ID = "channel_reminders"
        private const val CHANNEL_REMINDERS_NAME = "Reminders"
        const val CHANNEL_EXPORT_DONE_ID = "channel_export_done"
        private const val CHANNEL_EXPORT_DONE_NAME = "Export done"
        private var permissionGranted = false

        fun setupNotificationChannel(activity: ComponentActivity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Channel for reminders
                val channelReminders = NotificationChannel(
                    CHANNEL_REMINDERS_ID,
                    CHANNEL_REMINDERS_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                val notificationManager = activity.getSystemService(Context.NOTIFICATION_SERVICE)
                        as NotificationManager
                notificationManager.createNotificationChannel(channelReminders)
                // Channel for export done notification
                val channelExportDone = NotificationChannel(
                    CHANNEL_EXPORT_DONE_ID,
                    CHANNEL_EXPORT_DONE_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager.createNotificationChannel(channelExportDone)
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

        fun scheduleDailyNotification(context: Context, hour: Int, minute: Int) {
            val currentTime = Calendar.getInstance()
            val dueTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
            }
            if (currentTime > dueTime)
                dueTime.add(Calendar.DAY_OF_MONTH, 1)
            val initialDelay = dueTime.timeInMillis - currentTime.timeInMillis
            val formattedTime = formatTimeDifference(initialDelay)
            if (BuildConfig.DEBUG) {
                Toast.makeText(
                    context,
                    "Notification in $formattedTime",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val workRequest = PeriodicWorkRequestBuilder<CheckExpirationsWorker>(
                1, TimeUnit.DAYS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()
            WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                    CheckExpirationsWorker.WORKER_ID,
                    ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                    workRequest
                )
        }

        private fun formatTimeDifference(timeDifference: Long): String {
            val days = TimeUnit.MILLISECONDS.toDays(timeDifference)
            val hours = TimeUnit.MILLISECONDS.toHours(timeDifference) % 24
            val minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifference) % 60
            val seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifference) % 60
            val formattedTime = StringBuilder()
            if (days > 0) {
                formattedTime.append("$days day${if (days > 1) "s" else ""} ")
            }
            if (hours > 0) {
                formattedTime.append("$hours hour${if (hours > 1) "s" else ""} ")
            }
            if (minutes > 0) {
                formattedTime.append("$minutes minute${if (minutes > 1) "s" else ""} ")
            }
            if (seconds > 0) {
                formattedTime.append("$seconds second${if (seconds > 1) "s" else ""} ")
            }
            return formattedTime.toString().trim()
        }

    }

}