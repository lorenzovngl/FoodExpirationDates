package com.lorenzovainigli.foodexpirationdates.util

import android.util.Log

object FirebaseUtils {

    private const val TAG = "FirebaseCrashlytics"

    fun logToCrashlytics(message: String) {
        try {
            val crashlyticsClass = Class.forName("com.google.firebase.crashlytics.FirebaseCrashlytics")
            val instanceMethod = crashlyticsClass.getMethod("getInstance")
            val crashlyticsInstance = instanceMethod.invoke(null)
            val logMethod = crashlyticsClass.getMethod("log", String::class.java)
              val recordExceptionMethod = crashlyticsClass.getMethod("recordException", Throwable::class.java)
            logMethod.invoke(crashlyticsInstance, message)
            recordExceptionMethod.invoke(crashlyticsInstance, RuntimeException(message))
            Log.i(TAG, "Logged message to Crashlytics: $message")
        } catch (e: ClassNotFoundException) {
            Log.i(TAG, "FirebaseCrashlytics SDK not found in this build variant")
        } catch (e: NoSuchMethodException) {
            Log.w(TAG, "Method not found in FirebaseCrashlytics SDK")
        } catch (e: Exception) {
            Log.e(TAG, "Error logging message to Crashlytics", e)
        }
    }

}