package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.CHANNEL_REMINDERS_ID
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.scheduleDailyNotification
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.showNotification
import kotlinx.coroutines.flow.first
import java.time.Clock
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltWorker
class CheckExpirationsWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val repository: ExpirationDateRepository,
    private val clock: Clock
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val nowMs = clock.millis()

        val msInADay = TimeUnit.DAYS.toMillis(1)
        val todayStartMs = nowMs
        val yesterdayMs = todayStartMs - msInADay
        val twoDaysAgoMs = todayStartMs - (2 * msInADay)
        val tomorrowMs = todayStartMs + msInADay

        val items = repository.getAll().first()
        val expiringItems = items.filter {
            computeExpirationDate(it) < tomorrowMs
        }

        val context = getLocalizedContext()

        if (expiringItems.isEmpty()) {
            scheduleNextRun(context)
            return Result.success()
        }

        val message = buildExpirationMessage(expiringItems, todayStartMs, yesterdayMs, twoDaysAgoMs, msInADay)

        showNotification(
            context = context,
            channelId = CHANNEL_REMINDERS_ID,
            title = context.getString(R.string.your_food_is_expiring),
            message = message
        )

        scheduleNextRun(context)
        return Result.success()
    }

    private fun scheduleNextRun(context: Context) {
        scheduleDailyNotification(
            context = context,
            hour = PreferencesRepository.getUserNotificationTimeHour(context),
            minute = PreferencesRepository.getUserNotificationTimeMinute(context))
    }

    private fun buildExpirationMessage(
        items: List<ExpirationDate>,
        todayMs: Long,
        yesterdayMs: Long,
        twoDaysAgoMs: Long,
        msInADay: Long
    ): String {
        val joinedItems = items.joinToString(", ") { item ->
            val expDateMs = computeExpirationDate(item)
            val relativeTime = when {
                expDateMs < twoDaysAgoMs -> {
                    val days = (todayMs - expDateMs) / msInADay
                    applicationContext.getString(R.string.n_days_ago, days)
                }
                expDateMs < yesterdayMs -> applicationContext.getString(R.string.yesterday).lowercase()
                expDateMs < todayMs -> applicationContext.getString(R.string.today).lowercase()
                else -> applicationContext.getString(R.string.tomorrow).lowercase()
            }
            "${item.foodName} ($relativeTime)"
        }

        return "$joinedItems."
    }

    private fun getLocalizedContext(): Context {
        return if (BuildConfig.DEBUG) {
            LocaleHelper.setLocale(
                context = applicationContext,
                language = PreferencesRepository.getLanguage(applicationContext)
            )
        } else {
            applicationContext
        }
    }

    companion object {
        const val WORKER_ID = "DailyExpirationCheck"
    }

}