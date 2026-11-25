package com.lorenzovainigli.foodexpirationdates.model.repository

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExpirationDateRepositoryTest {

    private lateinit var dao: ExpirationDatesDao
    private lateinit var database: AppDatabase
    private lateinit var repository: ExpirationDateRepository

    @Before
    fun setup() {
        // Set up the in-memory Room database for testing
        database = inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
        dao = database.expirationDatesDao
        repository = ExpirationDatesRepositoryImpl(dao)
    }

    @After
    fun teardown() {
        // Close the Room database after testing
        database.close()
    }

    @Test
    fun insertExpirationDateTest() = runTest {
        val foodName = "Cheese"
        val expirationDate = System.currentTimeMillis()

        val insertedItem = ExpirationDate(
            id = 0,
            foodName = foodName,
            expirationDate = expirationDate
        )

        repository.addExpirationDate(insertedItem)

        val list = dao.getAll().first()

        assertEquals(1, list.size)
        assertEquals(foodName, list[0].foodName)
        assertEquals(expirationDate, list[0].expirationDate)
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

        val item = dao.getAll().first().single()
        val insertedId = item.id

        item.foodName = "Tomato"
        item.expirationDate = expirationDate + 1

        repository.addExpirationDate(item)

        val list = dao.getAll().first()

        assertEquals(1, list.size)
        assertEquals(insertedId, list[0].id)
        assertEquals("Tomato", list[0].foodName)
        assertEquals(expirationDate + 1, list[0].expirationDate)
    }

    @Test
    fun deleteExpirationDateTest() = runTest {
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = System.currentTimeMillis()
        )

        dao.insert(insertedItem)

        val item = dao.getAll().first().single()

        repository.deleteExpirationDate(item.id)

        val list = dao.getAll().first()

        assertTrue(list.none { it.id == item.id })
        assertEquals(0, list.size)
    }

    @Test
    fun deleteAllTest() = runTest {
        val item1 = ExpirationDate(
            id = 1,
            foodName = "Cheese",
            expirationDate = System.currentTimeMillis()
        )

        val item2 = ExpirationDate(
            id = 2,
            foodName = "Milk",
            expirationDate = System.currentTimeMillis()
        )

        repository.addExpirationDate(item1)
        repository.addExpirationDate(item2)

        assertEquals(2, dao.getAll().first().size)

        repository.deleteAll()

        assertEquals(0, dao.getAll().first().size)
        assertEquals(0, repository.getAll().first().size)
    }
}