package com.lorenzovainigli.foodexpirationdates.model.repository

import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExpirationDatesRepositoryImpl @Inject constructor(
    private val expirationDateDao: ExpirationDatesDao
    ) : ExpirationDateRepository {

    override suspend fun getAll(): Flow<List<ExpirationDate>> {
        return expirationDateDao.getAll()
    }

    override suspend fun getOne(id: Int): ExpirationDate {
        return expirationDateDao.getOne(id)
    }

    override suspend fun addExpirationDate(expirationDate: ExpirationDate) {
        expirationDateDao.insert(expirationDate)
    }

    override suspend fun deleteExpirationDate(expirationDate: ExpirationDate) {
        expirationDateDao.delete(expirationDate)
    }
}