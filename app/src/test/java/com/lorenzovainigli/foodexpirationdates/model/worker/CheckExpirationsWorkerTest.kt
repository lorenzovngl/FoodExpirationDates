package com.lorenzovainigli.foodexpirationdates.model.worker

import android.content.Context
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import com.lorenzovainigli.foodexpirationdates.R
import com.lorenzovainigli.foodexpirationdates.model.LocaleHelper
import com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion.scheduleDailyNotification
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.computeExpirationDate
import com.lorenzovainigli.foodexpirationdates.showNotification
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class CheckExpirationsWorkerTest {

    private lateinit var worker: CheckExpirationsWorker

    // Mock dependencies
    private val mockContext = mockk<Context>(relaxed = true)
    private val mockParams = mockk<WorkerParameters>(relaxed = true)
    private val mockRepository = mockk<ExpirationDateRepository>()

    // Fix the time
    private val fixedInstant = Instant.parse("2026-06-01T10:00:00Z")
    private val fixedClock = Clock.fixed(fixedInstant, ZoneId.of("UTC"))

    // Time constants based on the fixed clock
    private val msInADay = 86400000L
    private val todayMs = fixedInstant.toEpochMilli()

    @Before
    fun setUp() {
        // Mock LocaleHelper to intercept Android Configuration changes
        mockkObject(LocaleHelper)
        every { LocaleHelper.setLocale(context = any(), language = any()) } returns mockContext

        // Mock top-level (static) functions
        mockkStatic("com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDateKt")
        mockkStatic("com.lorenzovainigli.foodexpirationdates.UtilsKt")

        every { mockContext.getString(R.string.today) } returns "today"
        every { mockContext.getString(R.string.tomorrow) } returns "tomorrow"
        every { mockContext.getString(R.string.yesterday) } returns "yesterday"
        every { mockContext.getString(R.string.your_food_is_expiring) } returns "Food is expiring!"
        every { mockContext.getString(R.string.n_days_ago, any()) } answers {
            val varargs = args[1] as Array<*>
            "${varargs[0]} days ago"
        }

        every { showNotification(any(), any(), any(), any()) } returns Unit

        mockkObject(com.lorenzovainigli.foodexpirationdates.model.NotificationManager.Companion)
        every {
            scheduleDailyNotification(any(), any(), any())
        } returns Unit

        every { mockContext.applicationContext } returns mockContext

        worker = CheckExpirationsWorker(
            appContext = mockContext,
            params = mockParams,
            repository = mockRepository,
            clock = fixedClock
        )
    }

    @After
    fun tearDown() {
        // Clear static mocks to avoid polluting other tests
        unmockkAll()
    }

    @Test
    fun `doWork returns success without notification when database is empty`() = runTest {
        coEvery { mockRepository.getAll() } returns flowOf(emptyList())
        val result = worker.doWork()
        assertEquals(Result.success(), result)
        verify(exactly = 0) {
            showNotification(any(), any(), any(), any())
        }
    }

    @Test
    fun `doWork returns success without notification when no items are expiring soon`() = runTest {
        val safeFood = ExpirationDate(
            id = 1,
            foodName = "Pasta",
            expirationDate = todayMs + (7 * msInADay)
        )

        every { computeExpirationDate(safeFood) } returns todayMs + (7 * msInADay)

        coEvery { mockRepository.getAll() } returns flowOf(listOf(safeFood))

        val result = worker.doWork()

        assertEquals(Result.success(), result)
        verify(exactly = 0) {
            showNotification(any(), any(), any(), any())
        }
    }

    @Test
    fun `doWork formats message correctly when there is only a single expiring item`() = runTest {
        val expirationToday = todayMs - 1000L
        val singleFood = ExpirationDate(id = 1, foodName = "Milk", expirationDate = expirationToday)

        every { computeExpirationDate(singleFood) } returns expirationToday
        coEvery { mockRepository.getAll() } returns flowOf(listOf(singleFood))

        val result = worker.doWork()

        assertEquals(Result.success(), result)

        val expectedMessage = "Milk (today)."

        verify(exactly = 1) {
            showNotification(
                context = any(),
                channelId = any(),
                title = "Food is expiring!",
                message = expectedMessage
            )
        }
    }

    @Test
    fun `doWork correctly formats multiple items sharing the exact same expiration window`() = runTest {
        // Arrange: Two distinct items both expiring today
        val expirationToday = todayMs - 1000L
        val firstFood = ExpirationDate(id = 1, foodName = "Milk", expirationDate = expirationToday)
        val secondFood = ExpirationDate(id = 2, foodName = "Yogurt", expirationDate = expirationToday)

        every { computeExpirationDate(firstFood) } returns expirationToday
        every { computeExpirationDate(secondFood) } returns expirationToday

        coEvery { mockRepository.getAll() } returns flowOf(listOf(firstFood, secondFood))

        val result = worker.doWork()

        assertEquals(Result.success(), result)

        val expectedMessage = "Milk (today), Yogurt (today)."

        verify(exactly = 1) {
            showNotification(
                context = any(),
                channelId = any(),
                title = "Food is expiring!",
                message = expectedMessage
            )
        }
    }

    @Test
    fun `doWork formats message correctly and shows notification for mixed expirations`() = runTest {
        val oneHour = 60 * 60 * 1000L

        val expirationTomorrow = todayMs + oneHour
        val expirationToday = todayMs - oneHour
        val expirationYesterday = todayMs - msInADay - oneHour
        val expirationPast = todayMs - (3 * msInADay)
        val expirationSafe = todayMs + (2 * msInADay)

        val foodToday = ExpirationDate(id = 1, foodName = "Milk", expirationDate = expirationToday)
        val foodTomorrow = ExpirationDate(id = 2, foodName = "Eggs", expirationDate = expirationTomorrow)
        val foodYesterday = ExpirationDate(id = 3, foodName = "Yogurt", expirationDate = expirationYesterday)
        val foodPast = ExpirationDate(id = 4, foodName = "Cheese", expirationDate = expirationPast)
        val foodSafe = ExpirationDate(id = 5, foodName = "Rice", expirationDate = expirationSafe)

        every { computeExpirationDate(foodToday) } returns expirationToday
        every { computeExpirationDate(foodTomorrow) } returns expirationTomorrow
        every { computeExpirationDate(foodYesterday) } returns expirationYesterday
        every { computeExpirationDate(foodPast) } returns expirationPast
        every { computeExpirationDate(foodSafe) } returns expirationSafe

        coEvery { mockRepository.getAll() } returns flowOf(
            listOf(foodToday, foodTomorrow, foodYesterday, foodPast, foodSafe)
        )

        val result = worker.doWork()

        assertEquals(Result.success(), result)

        val expectedMessage = "Milk (today), Eggs (tomorrow), Yogurt (yesterday), Cheese (3 days ago)."

        verify(exactly = 1) {
            showNotification(
                context = any(),
                channelId = any(),
                title = "Food is expiring!",
                message = expectedMessage
            )
        }
    }

    @Test
    fun `doWork accurately calculates relative days elapsed for heavily outdated items`() = runTest {
        val daysPast = 45L
        val expirationPast = todayMs - (daysPast * msInADay) - 1000L

        val oldFood = ExpirationDate(id = 1, foodName = "Canned Beans", expirationDate = expirationPast)

        every { computeExpirationDate(oldFood) } returns expirationPast
        coEvery { mockRepository.getAll() } returns flowOf(listOf(oldFood))

        val result = worker.doWork()

        assertEquals(Result.success(), result)

        val expectedMessage = "Canned Beans (45 days ago)."

        verify(exactly = 1) {
            showNotification(
                context = any(),
                channelId = any(),
                title = "Food is expiring!",
                message = expectedMessage
            )
        }
    }

}