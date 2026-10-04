package com.lorenzovainigli.foodexpirationdates.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.lorenzovainigli.foodexpirationdates.PLAY_STORE_URL
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewManager
import javax.inject.Inject

class ReviewManagerImpl @Inject constructor() : ReviewManager {
    override fun requestReview(
        activity: Activity,
        isAutomatic: Boolean,
        foodCount: Int?,
        onComplete: () -> Unit
    ) {
        if (!isAutomatic) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL))
            activity.startActivity(intent)
        }
        onComplete()
    }
}
