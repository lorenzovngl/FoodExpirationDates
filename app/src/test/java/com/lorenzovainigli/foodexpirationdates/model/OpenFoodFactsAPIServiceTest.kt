package com.lorenzovainigli.foodexpirationdates.model

import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenFoodFactsAPIServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var service: OpenFoodFactsAPIService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsAPIService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProduct sends correct request and parses response`() = runTest {
        // GIVEN
        val barcode = "123456789"
        val jsonBody = """
            {
              "code": $barcode,
              "product": {
                "brands": "Test Brand",
                "product_name": "Test Product",
                "code": $barcode,
                "image_thumb_url": "https://example.com/image.jpg"
              }
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonBody)
        )

        // WHEN
        val response = service.getProduct(barcode)

        // THEN
        val request = mockWebServer.takeRequest()
        assertEquals(
            "/$barcode?fields=brands,product_name,code,image_thumb_url",
            request.path
        )
        assertEquals("GET", request.method)

        assertTrue(response.isSuccessful)
        assertEquals(barcode, response.body()?.code)
    }

    @Test
    fun `getProduct returns status 0 when product not found`() = runTest {
        // GIVEN
        val barcode = "00301762"
        val jsonBody = """
            {
              "code": "00301762",
              "status": 0,
              "status_verbose": "product not found"
            }
        """.trimIndent()

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonBody)
        )

        // WHEN
        val response = service.getProduct(barcode)

        // THEN: request is correct
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertEquals(
            "/$barcode?fields=brands,product_name,code,image_thumb_url",
            request.path
        )

        // THEN: response is successful but indicates "not found" in body
        assertTrue(response.isSuccessful)

        val body = response.body()
        assertNotNull(body)

        assertEquals(barcode, body!!.code)
        assertEquals(0, body.status)
        assertEquals("product not found", body.statusVerbose)
    }

}