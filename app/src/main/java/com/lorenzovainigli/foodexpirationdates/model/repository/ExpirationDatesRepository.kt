package com.lorenzovainigli.foodexpirationdates.model.repository

import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.coroutines.flow.Flow

interface ExpirationDateRepository {

    suspend fun getAll(): Flow<List<ExpirationDate>>

    suspend fun getOne(id: Int): ExpirationDate

    suspend fun addExpirationDate(expirationDate: ExpirationDate)

    suspend fun deleteExpirationDate(expirationDate: ExpirationDate)

    fun getItemsExpiringWithinOneDay(
        currentTimeMillis: Long,
        nextDayMillis: Long
    ): Flow<List<ExpirationDate>>
}