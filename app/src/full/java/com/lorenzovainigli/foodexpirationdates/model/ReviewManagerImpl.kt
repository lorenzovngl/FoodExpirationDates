package com.lorenzovainigli.foodexpirationdates.model

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.google.android.play.core.review.ReviewManagerFactory
import com.lorenzovainigli.foodexpirationdates.PLAY_STORE_URL
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsEvent
import com.lorenzovainigli.foodexpirationdates.analytics.AnalyticsTracker
import com.lorenzovainigli.foodexpirationdates.model.review.ReviewManager
import javax.inject.Inject

class ReviewManagerImpl @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) : ReviewManager {
    override fun requestReview(
        activity: Activity,
        isAutomatic: Boolean,
        onComplete: () -> Unit
    ) {
        analyticsTracker.logEvent(
            AnalyticsEvent.REVIEW_REQUESTED,
            mapOf("automatic" to isAutomatic)
        )
        val manager = ReviewManagerFactory.create(activity)
        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    analyticsTracker.logEvent(
                        AnalyticsEvent.REVIEW_FLOW_COMPLETED,
                        mapOf("automatic" to isAutomatic)
                    )
                    onComplete()
                }
            } else {
                analyticsTracker.logEvent(
                    AnalyticsEvent.REVIEW_REQUEST_FAILED,
                    mapOf("automatic" to isAutomatic)
                )
                if (!isAutomatic) {
                    // Fallback to browser for manual requests if SDK fails
                    analyticsTracker.logEvent(
                        AnalyticsEvent.REVIEW_STORE_FALLBACK_OPENED
                    )
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL))
                    activity.startActivity(intent)
                }
                onComplete()
            }
        }
    }
}
