package com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.mapper

import android.content.Context
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.ExpirationStatus
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.presentation.model.FoodCardUiModel
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.PreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class FoodCardUiModelMapper @Inject constructor(
    @ApplicationContext private val context: Context,
    private val clock: Clock = Clock.systemDefaultZone(),
) {

    fun map(item: ExpirationDate): FoodCardUiModel {
        val zoneId = clock.zone
        val today = LocalDate.now(clock)

        val expirationTimestamp = computeExpirationDate(item)
        val expirationDate = expirationTimestamp.toLocalDate(zoneId)

        val daysUntilExpiration = ChronoUnit.DAYS
            .between(today, expirationDate)
            .toInt()

        return FoodCardUiModel(
            id = item.id,
            foodName = item.foodName,
            quantity = item.quantity,
            expirationText = formatExpirationText(
                expirationTimestamp = expirationTimestamp,
                daysUntilExpiration = daysUntilExpiration,
            ),
            daysUntilExpiration = daysUntilExpiration,
            isOpened = item.openingDate != null,
            expirationStatus = when {
                daysUntilExpiration < 0 -> ExpirationStatus.EXPIRED
                daysUntilExpiration < 1 -> ExpirationStatus.EXPIRES_TODAY
                daysUntilExpiration <= 7 -> ExpirationStatus.EXPIRING_SOON
                else -> ExpirationStatus.VALID
            },
        )
    }

    private fun formatExpirationText(
        expirationTimestamp: Long,
        daysUntilExpiration: Int,
    ): String =
        when {
            daysUntilExpiration < -1 ->
                context.getString(
                    R.string.n_days_ago,
                    -daysUntilExpiration,
                )

            daysUntilExpiration == -1 ->
                context.getString(R.string.yesterday)

            daysUntilExpiration == 0 ->
                context.getString(R.string.today)

            daysUntilExpiration == 1 ->
                context.getString(R.string.tomorrow)

            daysUntilExpiration in 2..6 ->
                context.getString(
                    R.string.in_n_days,
                    daysUntilExpiration,
                )

            else -> formatDate(expirationTimestamp)
        }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = PreferencesRepository.getUserDateFormat(context)
        val locale = context.resources.configuration.locales[0]

        return SimpleDateFormat(dateFormat, locale).format(timestamp)
    }
}

private fun Long.toLocalDate(zoneId: ZoneId): LocalDate =
    Instant
        .ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
