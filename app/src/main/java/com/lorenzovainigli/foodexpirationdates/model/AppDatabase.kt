package com.lorenzovainigli.foodexpirationdates.model

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate

@Database(
    entities = [ExpirationDate::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val expirationDatesDao: ExpirationDatesDao
}