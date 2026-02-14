package com.lorenzovainigli.foodexpirationdates.util

import com.google.mlkit.vision.barcode.common.Barcode
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class BarcodeValidatorTest {

    private val validator = BarcodeValidator()

    // --- Helpers ---
    private fun mockBarcode(
        format: Int,
        rawValue: String? = null,
        displayValue: String? = null
    ): Barcode = mock {
        on { this.format } doReturn format
        on { this.rawValue } doReturn rawValue
        on { this.displayValue } doReturn displayValue
    }

    // Known-valid EAN-13 (checksum correct): 4006381333931
    private val validEan13 = "4006381333931"
    // Invalid checksum variant (last digit changed): 4006381333932
    private val invalidEan13 = "4006381333932"

    // -------------------------
    // Supported formats: HAPPY PATH
    // -------------------------

    @Test
    fun `EAN_13 valid digits length and checksum returns true`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = validEan13)
        assertTrue(validator.isValidBarcode(b))
    }

    @Test
    fun `EAN_13 with spaces and newlines is normalized and returns true`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "  $validEan13 \n")
        assertTrue(validator.isValidBarcode(b))
    }

    @Test
    fun `EAN_8 digits length returns true`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_8, rawValue = "96385074") // 8 digits
        assertTrue(validator.isValidBarcode(b))
    }

    @Test
    fun `UPC_A digits length returns true`() {
        val b = mockBarcode(Barcode.FORMAT_UPC_A, rawValue = "042100005264") // 12 digits
        assertTrue(validator.isValidBarcode(b))
    }

    @Test
    fun `UPC_E digits length returns true`() {
        val b = mockBarcode(Barcode.FORMAT_UPC_E, rawValue = "04252614") // commonly 8 digits in many payloads
        assertTrue(validator.isValidBarcode(b))
    }

    // -------------------------
    // Format filtering
    // -------------------------

    @Test
    fun `Unsupported format returns false`() {
        val b = mockBarcode(Barcode.FORMAT_QR_CODE, rawValue = validEan13)
        assertFalse(validator.isValidBarcode(b))
    }

    // -------------------------
    // Raw extraction cases
    // -------------------------

    @Test
    fun `Null rawValue but displayValue present is used`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = null, displayValue = validEan13)
        assertTrue(validator.isValidBarcode(b))
    }

    @Test
    fun `Both rawValue and displayValue null returns false`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = null, displayValue = null)
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Blank payload returns false`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "   ")
        assertFalse(validator.isValidBarcode(b))
    }

    // -------------------------
    // URL rejection
    // -------------------------

    @Test
    fun `Plain http URL is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "http://example.com")
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Plain https URL is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "https://example.com")
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Encoded URL is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "http%3A%2F%2Fexample.com")
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Mixed encoded URL is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "http:%2F%2Fexample.com")
        assertFalse(validator.isValidBarcode(b))
    }

    // -------------------------
    // Digits + length validation
    // -------------------------

    @Test
    fun `Non-digit payload is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "40063A1333931")
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Too short payload is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "1234567") // 7
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `Too long payload is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = "123456789012345") // 15
        assertFalse(validator.isValidBarcode(b))
    }

    // -------------------------
    // EAN-13 checksum (only if your isValidBarcode implements it)
    // If you didn't add checksum validation, delete these two tests.
    // -------------------------

    @Test
    fun `EAN_13 invalid checksum is rejected`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_13, rawValue = invalidEan13)
        assertFalse(validator.isValidBarcode(b))
    }

    @Test
    fun `EAN_8 does not require EAN_13 checksum and can pass digits length`() {
        val b = mockBarcode(Barcode.FORMAT_EAN_8, rawValue = "12345670")
        assertTrue(validator.isValidBarcode(b))
    }

}