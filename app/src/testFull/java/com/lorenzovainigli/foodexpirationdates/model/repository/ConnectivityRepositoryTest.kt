package com.lorenzovainigli.foodexpirationdates.model.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ConnectivityRepositoryTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var repository: ConnectivityRepository

    // A 'slot' allows us to capture the anonymous NetworkCallback
    // that the Repository creates internally, so we can trigger it manually.
    private val callbackSlot = slot<ConnectivityManager.NetworkCallback>()

    @Before
    fun setUp() {
        // 1. Mock the Android dependencies
        context = mockk()
        connectivityManager = mockk(relaxed = true)

        // 2. Tell the Context to return our mocked ConnectivityManager
        every {
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
        } returns connectivityManager

        // 3. Intercept the registration and capture the callback into our slot
        every {
            connectivityManager.registerDefaultNetworkCallback(capture(callbackSlot))
        } answers { }

        // 4. Instantiate the repository
        repository = ConnectivityRepositoryImpl(context)
    }

    @Test
    fun `when network becomes available, flow emits true`() = runTest {
        // .test {} comes from Turbine. It collects the flow and allows us to await items.
        repository.isConnected.test {

            // Simulate Android OS firing the 'onAvailable' event
            val mockNetwork = mockk<Network>()
            callbackSlot.captured.onAvailable(mockNetwork)

            // Assert that our Flow emitted 'true'
            assertEquals(true, awaitItem())
        }
    }

    @Test
    fun `when network is lost, flow emits false`() = runTest {
        repository.isConnected.test {

            // Simulate Android OS firing the 'onLost' event
            val mockNetwork = mockk<Network>()
            callbackSlot.captured.onLost(mockNetwork)

            // Assert that our Flow emitted 'false'
            assertEquals(false, awaitItem())
        }
    }

    @Test
    fun `when flow collection is cancelled, callback is properly unregistered`() = runTest {
        repository.isConnected.test {
            // Instantly cancel the Flow collection
            cancelAndIgnoreRemainingEvents()
        }

        // Verify that the 'awaitClose' block in our Repository was executed
        // and it successfully unregistered the EXACT same callback it registered.
        verify(exactly = 1) {
            connectivityManager.unregisterNetworkCallback(callbackSlot.captured)
        }
    }
}