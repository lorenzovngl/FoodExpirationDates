package com.lorenzovainigli.foodexpirationdates.analytics

import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.lorenzovainigli.foodexpirationdates.view.composable.screen.Screen
import javax.inject.Inject

class FirebaseAnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : AnalyticsTracker {

    override fun logEvent(event: AnalyticsEvent/*, params: Map<String, Any?>*/) {
        firebaseAnalytics.logEvent(event.tag) {
            /*params.forEach { (key, value) ->
                when (value) {
                    is String -> param(key, value)
                    is Long -> param(key, value)
                    is Int -> param(key, value.toLong())
                    is Double -> param(key, value)
                    is Boolean -> param(key, value.toString())
                }
            }*/
        }
        Log.d(TAG, "Logged event: ${event.tag}"/* with params: $params"*/)
    }

    override fun logScreenView(screen: Screen) {
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screen.name)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, screen.name)
        }
        Log.d(TAG, "Logged screen view: ${screen.name}")
    }

    companion object {
        const val TAG = "FirebaseAnalyticsTracker"
    }
}