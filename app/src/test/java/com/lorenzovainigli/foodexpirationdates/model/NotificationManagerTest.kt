package com.lorenzovainigli.foodexpirationdates.model

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.scheduleDailyNotification
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.worker.CheckExpirationsWorker
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.collections.emptyList

@RunWith(RobolectricTestRunner::class)
class NotificationWorkManagerTest {

    private lateinit var context: Context
    private lateinit var workManager: WorkManager

    // Mocks for dependencies
    private val mockRepository = mockk<ExpirationDateRepository>(relaxed = true)
    private val fixedClock = Clock.fixed(Instant.parse("2026-06-04T10:00:00Z"), ZoneId.of("UTC"))

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()

        // Create a custom WorkerFactory to manually inject our mocks into the HiltWorker
        val customWorkerFactory = object : WorkerFactory() {
            override fun createWorker(
                appContext: Context,
                workerClassName: String,
                workerParameters: WorkerParameters
            ): ListenableWorker? {
                return if (workerClassName == CheckExpirationsWorker::class.java.name) {
                    CheckExpirationsWorker(appContext, workerParameters, mockRepository, fixedClock)
                } else {
                    null
                }
            }
        }

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .setWorkerFactory(customWorkerFactory) // Tell WorkManager to use our factory
            .build()

        WorkManagerTestInitHelper.initializeTestWorkManager(context, config)
        workManager = WorkManager.getInstance(context)

        // Stub repository to return an empty list by default so the worker executes clean
        coEvery { mockRepository.getAll() } returns flowOf(emptyList())
    }

    @Test
    fun scheduleDailyNotification_shouldSuccessfullyEnqueueWorker() {
        scheduleDailyNotification(context, 14, 0)

        val workInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()

        assertEquals(1, workInfos.size)
        assertEquals(WorkInfo.State.ENQUEUED, workInfos.first().state)
    }

    @Test
    fun whenWorkerFires_replacePolicyShouldScheduleNextWorkerForTomorrow() {
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // 1. Queue up the initial notification run
        scheduleDailyNotification(context, 14, 0)

        var workInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()
        val firstWorkerId = workInfos.first().id

        // 2. Simulate Time progression to trip the execution threshold
        testDriver?.setInitialDelayMet(firstWorkerId)

        // 3. Refresh work registry status
        workInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()

        // Assert: The unique name constraint means exactly one worker remains managed
        assertEquals("Queue should contain exactly 1 worker entry post-execution", 1, workInfos.size)

        val newWorker = workInfos.first()

        // Assert: The old worker was kicked out by ExistingWorkPolicy.REPLACE via scheduleNextRun()
        assertNotEquals(
            "The worker ID should change, confirming replacement execution occurred",
            firstWorkerId,
            newWorker.id
        )

        assertEquals(
            "The upcoming worker chain link should be sitting patiently in ENQUEUED",
            WorkInfo.State.ENQUEUED,
            newWorker.state
        )
    }

    @Test
    fun whenRepositoryIsEmpty_shouldNotShowNotification_butMustScheduleNextCheck() {
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // 1. Setup: Mock the repository to return an empty list of food items
        coEvery { mockRepository.getAll() } returns flowOf(emptyList())

        // 2. Act: Schedule the initial notification run
        scheduleDailyNotification(context, 14, 0)

        // Capture the Unique ID of the first scheduled worker
        val firstWorkInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()
        val firstWorkerId = firstWorkInfos.first().id

        // Simulate time progression to force the worker to execute instantly
        testDriver?.setInitialDelayMet(firstWorkerId)

        // 3. Assert: Verify the Schelling/chain mechanism still triggers
        val updatedWorkInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()

        // Even though there was no food expiring, the queue must still contain exactly 1 worker
        assertEquals("Queue should contain exactly 1 worker even when database is empty", 1, updatedWorkInfos.size)

        val newWorker = updatedWorkInfos.first()

        // The worker ID must be completely new, proving that scheduleNextRun() was executed inside the empty-check block
        assertNotEquals(
            "The worker ID should change, confirming the next cycle was scheduled",
            firstWorkerId,
            newWorker.id
        )

        // The new worker must be waiting in the ENQUEUED state for tomorrow
        assertEquals(
            "The next chain link must be ENQUEUED for tomorrow",
            WorkInfo.State.ENQUEUED,
            newWorker.state
        )
    }

    @Test
    fun whenItemsAreExpiring_shouldShowNotification_andMustScheduleNextCheck() {
        val testDriver = WorkManagerTestInitHelper.getTestDriver(context)

        // 1. Setup: Create a fake food item expiring tomorrow
        // Since our fixedClock is locked at June 4th, 2026, we target June 5th, 2026.
        val tomorrowMs = Instant.parse("2026-06-05T10:00:00Z").toEpochMilli()

        val expiringItem = ExpirationDate(
            id = 1,
            foodName = "Milk",
            expirationDate = tomorrowMs // Assuming computeExpirationDate extracts this value
        )

        // Mock the repository to return the expiring milk item
        coEvery { mockRepository.getAll() } returns flowOf(listOf(expiringItem))

        // 2. Act: Schedule the initial notification run
        scheduleDailyNotification(context, 14, 0)

        // Capture the Unique ID of the first scheduled worker
        val firstWorkInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()
        val firstWorkerId = firstWorkInfos.first().id

        // Simulate time progression to execute the worker logic
        testDriver?.setInitialDelayMet(firstWorkerId)

        // 3. Assert: Verify the notification branch completes and chains the next execution
        val updatedWorkInfos = workManager.getWorkInfosForUniqueWork(CheckExpirationsWorker.WORKER_ID).get()

        // The queue must still maintain exactly 1 unique managed worker entry due to ExistingWorkPolicy.REPLACE
        assertEquals("Queue should contain exactly 1 worker post notification trigger", 1, updatedWorkInfos.size)

        val newWorker = updatedWorkInfos.first()

        // Confirm the replacement successfully took place after compiling the notification payload
        assertNotEquals(
            "The worker ID should change, proving a new notification cycle was successfully chained",
            firstWorkerId,
            newWorker.id
        )

        // Ensure the new scheduled task is waiting patiently in the queue
        assertEquals(
            "The upcoming worker must be sitting in the ENQUEUED state",
            WorkInfo.State.ENQUEUED,
            newWorker.state
        )
    }
}