package com.lorenzovainigli.foodexpirationdates.model

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE expiration_dates ADD COLUMN date_added INTEGER NOT NULL DEFAULT 0")
        // Update existing rows with current timestamp
        database.execSQL("UPDATE expiration_dates SET date_added = (strftime('%s', 'now') * 1000) WHERE date_added = 0")
    }
}

@Database(
    entities = [ExpirationDate::class],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3)
    ],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract val expirationDatesDao: ExpirationDatesDao
}