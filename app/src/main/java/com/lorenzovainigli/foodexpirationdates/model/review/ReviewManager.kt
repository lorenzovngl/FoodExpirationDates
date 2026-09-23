package com.lorenzovainigli.foodexpirationdates.model.review

import android.app.Activity

interface ReviewManager {
    fun requestReview(activity: Activity, isAutomatic: Boolean = false, onComplete: () -> Unit = {})
}