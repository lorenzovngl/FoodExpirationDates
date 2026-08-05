package com.lorenzovainigli.foodexpirationdates.model.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate

@Dao
interface ExpirationDatesDao {
    @Query("SELECT * FROM expiration_dates ORDER BY COALESCE(opening_date, expiration_date)")
    fun getAll(): Flow<List<ExpirationDate>>

    @Query("SELECT * FROM expiration_dates WHERE id = :id")
    fun getOne(id: Int): ExpirationDate

    @Insert
    fun insertAll(dates: List<ExpirationDate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(date: ExpirationDate)

    @Query("DELETE FROM expiration_dates WHERE id = :id")
    suspend fun deleteById(id: Int)
}