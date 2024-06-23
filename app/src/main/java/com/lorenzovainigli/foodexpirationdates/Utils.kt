package com.lorenzovainigli.foodexpirationdates

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.CHANNEL_REMINDERS_ID
import com.lorenzovainigli.foodexpirationdates.view.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.net.URL

const val DEVELOPER_EMAIL = "lorenzovngl@gmail.com"
const val WEBSITE_URL_EN = "https://foodexpirationdates.app/"
const val WEBSITE_URL_IT = "https://scadenzealimenti.app/"
const val GITHUB_URL = "https://github.com/lorenzovngl/FoodExpirationDates"
const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.lorenzovainigli.foodexpirationdates"
const val PRIVACY_POLICY_URL = "$GITHUB_URL/blob/main/privacy-policy.md"

fun showNotification(
    context: Context,
    channelId: String,
    title: String,
    message: String
) {
    if (ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
        val intent = when (channelId) {
            CHANNEL_REMINDERS_ID -> Intent(context, MainActivity::class.java)
            else -> Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentText(message)
            .setContentTitle(title)
            .setSmallIcon(
                when (channelId) {
                    CHANNEL_REMINDERS_ID -> R.drawable.fed_icon_notification
                    else -> R.drawable.ic_download
                },
            )
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(
            when (channelId) {
                CHANNEL_REMINDERS_ID -> 1
                else -> System.currentTimeMillis().toInt()
            },
            notification
        )
    }
}

fun saveFileToExternalStorage(context: Context, url: String, fileName: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        saveFileUsingMediaStore(context, url, fileName)
    } else {
        saveFileToExternalStorageLegacy(url, fileName)
    }
    showNotification(
        context = context,
        channelId = com.lorenzovainigli.foodexpirationdates.model.NotificationManager.CHANNEL_EXPORT_DONE_ID,
        title = context.getString(R.string.data_exported),
        message = fileName
    )
}

private fun saveFileToExternalStorageLegacy(url: String, fileName: String) {
    val target = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        fileName
    )
    URL(url).openStream().use { input ->
        FileOutputStream(target).use { output ->
            input.copyTo(output)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
private fun saveFileUsingMediaStore(context: Context, url: String, fileName: String) {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        URL(url).openStream().use { input ->
            resolver.openOutputStream(uri).use { output ->
                input.copyTo(output!!)
            }
        }
    }
}