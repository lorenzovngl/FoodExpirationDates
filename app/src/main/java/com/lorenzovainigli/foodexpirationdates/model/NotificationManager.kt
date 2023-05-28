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
import com.lorenzovainigli.foodexpirationdates.BuildConfig

class NotificationManager {

    companion object {

        private const val channelId = "channel_reminders"
        private const val channelName = "Reminders"
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

    }

}