package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.BuildConfig
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.CHANNEL_REMINDERS_ID
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import com.lorenzovainigli.foodexpirationdates.showNotification
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

@HiltWorker
class CheckExpirationsWorker @Inject constructor(
    appContext: Context,
    params: WorkerParameters,
    private val repository: ExpirationDateRepository
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val sb = StringBuilder()
        val today = Calendar.getInstance()
        val twoDaysAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -2)
        }
        val yesterday = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -1)
        }
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        val msInADay = (1000 * 60 * 60 * 24)
        val filteredList = repository.getAll().first().filter {
            computeExpirationDate(it) < tomorrow.time.time
        }
        if (filteredList.isEmpty()) {
            return Result.success()
        }
        filteredList.map {
            val expDate = computeExpirationDate(it)
            sb.append(it.foodName).append(" (")
            if (expDate < twoDaysAgo.time.time) {
                val days = (today.time.time - expDate) / msInADay
                sb.append(applicationContext.getString(R.string.n_days_ago, days))
            } else if (expDate < yesterday.time.time)
                sb.append(applicationContext.getString(R.string.yesterday).lowercase())
            else if (expDate < today.time.time) {
                sb.append(applicationContext.getString(R.string.today).lowercase())
            } else {
                sb.append(applicationContext.getString(R.string.tomorrow).lowercase())
            }
            sb.append("), ")
        }
        var message = ""
        if (sb.toString().length > 2)
            message = sb.toString().substring(0, sb.toString().length - 2) + "."
        val context = if (BuildConfig.DEBUG) {
            LocaleHelper.setLocale(
                context = applicationContext,
                language = PreferencesRepository.getLanguage(applicationContext)
            )
        } else applicationContext
        showNotification(
            context = context,
            channelId = CHANNEL_REMINDERS_ID,
            title = context.getString(R.string.your_food_is_expiring),
            message = message
        )
        return Result.success()
    }

    companion object {
        const val WORKER_ID = "DailyExpirationCheck"
    }

}