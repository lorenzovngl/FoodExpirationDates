package com.lorenzovainigli.foodexpirationdates.util

import android.app.Activity
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class AppReviewManager(private val activity: Activity) {

    private val reviewManager: ReviewManager = ReviewManagerFactory.create(activity)
    private var reviewInfo: ReviewInfo? = null

    fun preloadReviewFlow() {
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                reviewInfo = task.result
            }
        }
    }

    fun showReviewDialog() {
        reviewInfo?.let {
            val flow = reviewManager.launchReviewFlow(activity, it)
            flow.addOnCompleteListener { _ ->
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown.
            }
        } ?: preloadReviewFlow()
    }
}