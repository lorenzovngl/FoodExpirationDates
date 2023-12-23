package com.lorenzovainigli.foodexpirationdates.model

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class MigrationTest {

    private val TEST_DB = "migration-test"

    @JvmField
    @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java
    )

    @Test
    fun migrate1To2() {
        helper.createDatabase(TEST_DB, 1).apply {
            close()
        }
        helper.runMigrationsAndValidate(TEST_DB, 2, true)
    }
}