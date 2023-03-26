package com.lorenzovainigli.foodexpirationdates.model.notification

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.lorenzovainigli.foodexpirationdates.R
import java.time.ZoneId

class NotificationScheduler(
    private val activity: ComponentActivity? = null,
    private val context: Context
){

    companion object {
        const val notificationTitle = "NOTIFICATION_TITLE"
        const val notificationMessage = "NOTIFICATION_MESSAGE"
    }

    private val channelId = "channel_reminders"
    private val channelName = "Reminders"
    private var permissionGranted = false

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    fun setup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionGranted = activity?.let {
                ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } == PackageManager.PERMISSION_GRANTED
            if (permissionGranted){
                permissionGranted = activity?.let {
                    ContextCompat.checkSelfPermission(
                        it.applicationContext,
                        Manifest.permission.SCHEDULE_EXACT_ALARM
                    )
                } == PackageManager.PERMISSION_GRANTED
            }
        }
        val requestPermissionLauncher =
            activity?.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    Toast.makeText(activity, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
                permissionGranted = isGranted
            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
            requestPermissionLauncher?.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }
    }

    fun showNotification(title: String, message: String){
        // TODO It is not okay to bypass this control
//        if (permissionGranted) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            val notification = NotificationCompat.Builder(context, channelId)
                .setContentText(message)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
            notificationManager.notify(1, notification)
//        }
    }

    fun schedule(item: ScheduledNotification){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                putExtra(notificationTitle, item.title)
                putExtra(notificationMessage, item.message)
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                item.time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            )
            Toast.makeText(context, "Scheduled!", Toast.LENGTH_SHORT).show()
        } else {
            TODO("Not yet implemented")
        }
    }

    fun cancel(item: ScheduledNotification){
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        )
    }
}