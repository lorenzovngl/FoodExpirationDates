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
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltWorker
class CheckExpirationsWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val repository: ExpirationDateRepository,
    private val clock: Clock
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val zoneId = ZoneId.systemDefault()

        val today = Instant.ofEpochMilli(clock.millis())
            .atZone(zoneId)
            .toLocalDate()

        val tomorrow = today.plusDays(1)

        val context = getLocalizedContext()

        return try {
            val items = repository.getAll().first()

            val expiringItems = items.filter { item ->
                val expirationDate = Instant
                    .ofEpochMilli(computeExpirationDate(item))
                    .atZone(zoneId)
                    .toLocalDate()

                !expirationDate.isAfter(tomorrow)
            }

            if (expiringItems.isNotEmpty()) {
                val message = buildExpirationMessage(
                    items = expiringItems,
                    today = today,
                    zoneId = zoneId
                )

                showNotification(
                    context = context,
                    channelId = CHANNEL_REMINDERS_ID,
                    title = context.getString(R.string.your_food_is_expiring),
                    message = message
                )
            }

            Result.success()
        } catch (_: Exception){
            Result.success()
        } finally {
            scheduleNextRun(context)
        }
    }

    private fun scheduleNextRun(context: Context) {
        scheduleDailyNotification(context = context)
    }

    private fun buildExpirationMessage(
        items: List<ExpirationDate>,
        today: LocalDate,
        zoneId: ZoneId
    ): String {
        return items.joinToString(", ", postfix = ".") { item ->
            val expirationDate = Instant.ofEpochMilli(computeExpirationDate(item))
                .atZone(zoneId)
                .toLocalDate()

            val label = when {
                expirationDate == today -> applicationContext.getString(R.string.today).lowercase()
                expirationDate == today.plusDays(1) -> applicationContext.getString(R.string.tomorrow).lowercase()
                expirationDate == today.minusDays(1) -> applicationContext.getString(R.string.yesterday).lowercase()
                expirationDate.isBefore(today) -> {
                    val days = ChronoUnit.DAYS.between(expirationDate, today)
                    applicationContext.getString(R.string.n_days_ago, days)
                }
                else -> {
                    val daysUntil = ChronoUnit.DAYS.between(today, expirationDate)
                    applicationContext.getString(R.string.in_n_days, daysUntil)
                }
            }

            "${item.foodName} ($label)"
        }
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