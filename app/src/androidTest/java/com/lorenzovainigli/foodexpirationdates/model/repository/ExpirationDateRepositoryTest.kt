package com.lorenzovainigli.foodexpirationdates.model.repository

import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.lorenzovainigli.foodexpirationdates.model.AppDatabase
import com.lorenzovainigli.foodexpirationdates.model.dao.ExpirationDatesDao
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun insertExpirationDateTest() {
        val foodName = "Cheese"
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = foodName,
            expirationDate = expirationDate
        )
        val list: MutableList<ExpirationDate> = mutableListOf()
        runBlocking {
            repository.addExpirationDate(insertedItem)
            val retrievedItem = dao.getAll().firstOrNull()
            retrievedItem?.forEach {
                list.add(it)
            }
        }
        assertEquals(list.size, 1)
        assertEquals(list[0].foodName, foodName)
        assertEquals(list[0].expirationDate, expirationDate)
    }

    @Test
    fun updateExpirationDateTest() {
        var foodName = "Cheese"
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = foodName,
            expirationDate = expirationDate
        )
        dao.insert(insertedItem)
        var insertedId = 0
        runBlocking {
            val item = dao.getAll().firstOrNull()
            item?.forEach {
                insertedId = it.id
            }
        }
        insertedItem.id = insertedId
        foodName = "Tomato"
        insertedItem.foodName = foodName
        insertedItem.expirationDate = expirationDate + 1
        val list: MutableList<ExpirationDate> = mutableListOf()
        runBlocking {
            repository.addExpirationDate(insertedItem)
            val retrievedItem = dao.getAll().firstOrNull()
            retrievedItem?.forEach {
                list.add(it)
            }
        }
        assertEquals(list.size, 1)
        assertEquals(list[0].foodName, foodName)
        assertEquals(list[0].expirationDate, expirationDate + 1)
    }

    @Test
    fun deleteExpirationDateTest() {
        val expirationDate = System.currentTimeMillis()
        val insertedItem = ExpirationDate(
            id = 0,
            foodName = "Cheese",
            expirationDate = expirationDate
        )
        dao.insert(insertedItem)
        var insertedId = 0
        runBlocking {
            val item = dao.getAll().firstOrNull()
            item?.forEach {
                insertedId = it.id
            }
        }
        insertedItem.id = insertedId
        dao.delete(insertedItem)
        val list: MutableList<ExpirationDate> = mutableListOf()
        runBlocking {
            repository.deleteExpirationDate(insertedItem)
            val retrievedItem = dao.getAll().firstOrNull()
            retrievedItem?.forEach {
                list.add(it)
            }
        }
        assertEquals(list.size, 0)
        val retrievedItem = dao.getOne(id = insertedId)
        assertNull(retrievedItem)
    }
}