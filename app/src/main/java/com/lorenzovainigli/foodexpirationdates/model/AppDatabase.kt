package com.lorenzovainigli.foodexpirationdates.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate

@Database(
    entities = [ExpirationDate::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val expirationDatesDao: ExpirationDatesDao
}