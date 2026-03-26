package com.lorenzovainigli.foodexpirationdates.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.lorenzovainigli.foodexpirationdates.MainDispatcherRule
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsAPIService
import com.lorenzovainigli.foodexpirationdates.model.OpenFoodFactsJsonResponse
import com.lorenzovainigli.foodexpirationdates.model.Product
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class APIServiceViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val service: OpenFoodFactsAPIService = mockk()
    private lateinit var viewModel: APIServiceViewModel

    @Before
    fun setup() {
        // Mock Android's static Log class to prevent RuntimeExceptions in local tests
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        viewModel = APIServiceViewModel(service)
    }

    @After
    fun teardown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `run calls service and updates state to GOT_RESULT on success`() = runTest {
        val barcode = "123456"
        val mockProduct = OpenFoodFactsJsonResponse(
            code = barcode,
            product = Product(brands = "Brand", productName = "Product", imageThumbUrl = "url"),
            status = 1,
            statusVerbose = "OK"
        )
        val mockResponse = Response.success(mockProduct)

        val deferredResponse = CompletableDeferred<Response<OpenFoodFactsJsonResponse>>()
        coEvery { service.getProduct(barcode) } coAnswers { deferredResponse.await() }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredResponse.complete(mockResponse)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GOT_RESULT, awaitItem())
        }

        assertEquals(mockProduct, viewModel.product.value)
        assertTrue(viewModel.responseStatus.value)
    }

    @Test
    fun `run updates state to SCANNING_ERROR when response is successful but body is null`() = runTest {
        val barcode = "123456"
        // Create a successful response, but with a null body
        val mockResponse = Response.success<OpenFoodFactsJsonResponse>(null)
        val deferredResponse = CompletableDeferred<Response<OpenFoodFactsJsonResponse>>()

        coEvery { service.getProduct(barcode) } coAnswers { deferredResponse.await() }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredResponse.complete(mockResponse)
            assertEquals(APIServiceViewModel.BarcodeScannerState.SCANNING_ERROR, awaitItem())
        }
    }

    @Test
    fun `run updates state to SCANNING_ERROR when response is not successful`() = runTest {
        val barcode = "123456"
        // Create an HTTP error response (e.g., 404 Not Found)
        val mockResponse = Response.error<OpenFoodFactsJsonResponse>(404, mockk(relaxed = true))
        val deferredResponse = CompletableDeferred<Response<OpenFoodFactsJsonResponse>>()

        coEvery { service.getProduct(barcode) } coAnswers { deferredResponse.await() }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredResponse.complete(mockResponse)
            assertEquals(APIServiceViewModel.BarcodeScannerState.SCANNING_ERROR, awaitItem())
        }
    }

    @Test
    fun `run updates state to NO_CONNECTION when UnknownHostException is thrown`() = runTest {
        val barcode = "123456"
        val deferredException = CompletableDeferred<Unit>()

        coEvery { service.getProduct(barcode) } coAnswers {
            deferredException.await()
            throw java.net.UnknownHostException("No internet")
        }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredException.complete(Unit)
            assertEquals(APIServiceViewModel.BarcodeScannerState.NO_CONNECTION, awaitItem())
        }
    }

    @Test
    fun `run updates state to NETWORK_ERROR when IOException is thrown`() = runTest {
        val barcode = "123456"
        val deferredException = CompletableDeferred<Unit>()

        coEvery { service.getProduct(barcode) } coAnswers {
            deferredException.await()
            throw java.io.IOException("Timeout or network failure")
        }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredException.complete(Unit)
            assertEquals(APIServiceViewModel.BarcodeScannerState.NETWORK_ERROR, awaitItem())
        }
    }

    @Test
    fun `run updates state to SCANNING_ERROR when generic Exception is thrown`() = runTest {
        val barcode = "123456"
        val deferredException = CompletableDeferred<Unit>()

        coEvery { service.getProduct(barcode) } coAnswers {
            deferredException.await()
            throw IllegalStateException("Something completely unexpected happened")
        }

        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.run(barcode)
            assertEquals(APIServiceViewModel.BarcodeScannerState.GETTING_PRODUCT_INFO, awaitItem())
            deferredException.complete(Unit)
            assertEquals(APIServiceViewModel.BarcodeScannerState.SCANNING_ERROR, awaitItem())
        }
    }

    @Test
    fun `setBarcodeScannerState manually updates the stateFlow`() = runTest {
        viewModel.barcodeScannerState.test {
            assertEquals(APIServiceViewModel.BarcodeScannerState.READY_TO_SCAN, awaitItem())
            viewModel.setBarcodeScannerState(APIServiceViewModel.BarcodeScannerState.NO_CONNECTION)
            assertEquals(APIServiceViewModel.BarcodeScannerState.NO_CONNECTION, awaitItem())
        }
    }
}