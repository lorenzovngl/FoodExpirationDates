package com.lorenzovainigli.foodexpirationdates

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileOutputStream
import java.net.URL

const val DEVELOPER_EMAIL = "lorenzovngl@gmail.com"
const val GITHUB_URL = "https://github.com/lorenzovngl/FoodExpirationDates"
const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.lorenzovainigli.foodexpirationdates"
const val PRIVACY_POLICY_URL = "$GITHUB_URL/blob/main/privacy-policy.md"

fun saveFileToExternalStorage(context: Context, url: String, fileName: String){
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        saveFileUsingMediaStore(context, url, fileName)
    } else {
        saveFileToExternalStorageLegacy(url, fileName)
    }
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