package com.lorenzovainigli.foodexpirationdates.model.dao

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ExpirationDatesDaoTest {

    private var dao: ExpirationDatesDao? = null
    private var database: AppDatabase? = null

    @Before
    fun setup() {
        // Set up the in-memory Room database for testing
        database = inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        dao = database!!.expirationDatesDao
    }

    @After
    fun teardown() {
        // Close the Room database after testing
        database?.close()
    }

    @Test
    fun insertExpirationDateTest() {
        val id = 0
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = expirationDate
        )
        dao?.insert(insertedItem)
        val retrievedItem = dao?.getOne(id = id)
        if (retrievedItem != null) {
            assertEquals(insertedItem.foodName, retrievedItem.foodName)
            assertEquals(insertedItem.expirationDate, retrievedItem.expirationDate)
        }
    }

    @Test
    fun updateExpirationDateTest() {
        val id = 0
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = expirationDate
        )
        dao?.insert(insertedItem)
        val updatedItem = ExpirationDate(
            id = 0,
            foodName = "Milk",
            expirationDate = expirationDate + 1
        )
        dao?.insert(insertedItem)
        val retrievedItem = dao?.getOne(id = id)
        if (retrievedItem != null) {
            assertEquals(updatedItem.foodName, retrievedItem.foodName)
            assertEquals(updatedItem.expirationDate, retrievedItem.expirationDate)
        }
    }

    @Test
    fun deleteExpirationDateTest() {
        val id = 0
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = expirationDate
        )
        dao?.insert(insertedItem)
        dao?.delete(insertedItem)
        val retrievedItem = dao?.getOne(id = id)
        assertNull(retrievedItem)
    }
}