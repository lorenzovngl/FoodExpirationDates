package com.lorenzovainigli.foodexpirationdates.model.notification

import java.time.LocalDateTime

data class ScheduledNotification (
        val time: LocalDateTime,
        val title: String,
        val message: String
)
