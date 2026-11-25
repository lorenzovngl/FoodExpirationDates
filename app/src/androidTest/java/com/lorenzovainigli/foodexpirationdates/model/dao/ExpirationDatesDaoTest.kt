package com.lorenzovainigli.foodexpirationdates.model.dao

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExpirationDatesDaoTest {

    private lateinit var dao: ExpirationDatesDao
    private lateinit var database: AppDatabase

    @Before
    fun setup() {
        // Set up the in-memory Room database for testing
        database = inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()

        dao = database.expirationDatesDao
    }

    @After
    fun teardown() {
        // Close the Room database after testing
        database.close()
    }

    @Test
    fun insertExpirationDateTest() = runTest {
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = System.currentTimeMillis()
        )

        dao.insert(insertedItem)

        val retrievedItem = dao.getAll().first().single()

        assertEquals(insertedItem.foodName, retrievedItem.foodName)
        assertEquals(insertedItem.expirationDate, retrievedItem.expirationDate)
        assertEquals(insertedItem.quantity, retrievedItem.quantity)
    }

    @Test
    fun updateExpirationDateTest() = runTest {
        val expirationDate = System.currentTimeMillis()

        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = expirationDate
        )

        dao.insert(insertedItem)

        val insertedId = dao.getAll().first().single().id

        val updatedItem = ExpirationDate(
            id = insertedId,
            foodName = "Milk",
            expirationDate = expirationDate + 1
        )

        dao.insert(updatedItem)

        val retrievedItem = dao.getAll().first().single()

        assertEquals(insertedId, retrievedItem.id)
        assertEquals(updatedItem.foodName, retrievedItem.foodName)
        assertEquals(updatedItem.expirationDate, retrievedItem.expirationDate)
    }

    @Test
    fun deleteExpirationDateTest() = runTest {
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = System.currentTimeMillis()
        )

        dao.insert(insertedItem)

        val insertedId = dao.getAll().first().single().id

        assertTrue(dao.getAll().first().any { it.id == insertedId })

        dao.deleteById(insertedId)

        assertTrue(dao.getAll().first().none { it.id == insertedId })
        assertEquals(0, dao.getAll().first().size)
    }

    @Test
    fun deleteAllExpirationDatesTest() = runTest {
        val item1 = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = System.currentTimeMillis()
        )

        val item2 = ExpirationDate(
            id = 0,
            foodName = "Milk",
            expirationDate = System.currentTimeMillis()
        )

        dao.insert(item1)
        dao.insert(item2)

        assertEquals(2, dao.getAll().first().size)

        dao.deleteAll()

        assertEquals(0, dao.getAll().first().size)
    }
}