package com.lorenzovainigli.foodexpirationdates.view.activity

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.model.worker.CheckExpirationsWorker
import com.lorenzovainigli.foodexpirationdates.view.composable.activity.SettingsActivityLayout
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            SettingsActivityLayout(
                scheduleDailyNotification = ::scheduleDailyNotification
            )
        }
    }

    private fun scheduleDailyNotification(hour: Int, minute: Int) {
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
                applicationContext,
                "Notification in $formattedTime",
                Toast.LENGTH_SHORT
            ).show()
        }
        val workRequest = PeriodicWorkRequestBuilder<CheckExpirationsWorker>(
            1, TimeUnit.DAYS
        )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .build()
        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                CheckExpirationsWorker.workerID,
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