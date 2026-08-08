package com.lorenzovainigli.foodexpirationdates.model

import android.app.Activity

interface ReviewManager {
    fun requestReview(activity: Activity, isAutomatic: Boolean = false, onComplete: () -> Unit = {})
}
