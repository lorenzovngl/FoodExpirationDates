package com.lorenzovainigli.foodexpirationdates.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
import com.lorenzovainigli.foodexpirationdates.PLAY_STORE_URL
import javax.inject.Inject

class ReviewManagerImpl @Inject constructor() : ReviewManager {
    override fun requestReview(activity: Activity, isAutomatic: Boolean, onComplete: () -> Unit) {
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    onComplete()
                }
            } else {
                if (!isAutomatic) {
                    // Fallback to browser for manual requests if SDK fails
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL))
                    activity.startActivity(intent)
                }
                onComplete()
            }
        }
    }
}
