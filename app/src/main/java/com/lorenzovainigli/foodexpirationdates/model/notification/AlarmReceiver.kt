package com.lorenzovainigli.foodexpirationdates.model.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(NotificationScheduler.notificationTitle) ?: ""
        val message = intent?.getStringExtra(NotificationScheduler.notificationMessage) ?: ""
        context?.let {
            val notificationController = NotificationScheduler(context = it)
            notificationController.showNotification(title = title, message = message)
        }
    }

}