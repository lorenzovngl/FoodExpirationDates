package com.lorenzovainigli.foodexpirationdates.util

import com.google.mlkit.vision.barcode.common.Barcode

class BarcodeValidator {

    private fun normalize(raw: String): String =
        raw.trim().replace(" ", "")

    private fun looksLikeBarcode(value: String): Boolean {
        // EAN-8..EAN-13 / UPC-A(12) etc.
        return value.length in 8..14 && value.all { it.isDigit() }
    }

    private fun looksLikeUrl(value: String): Boolean {
        val v = value.lowercase()
        return v.startsWith("http://") || v.startsWith("https://") || v.startsWith("http%3a") || v.startsWith("http%2f") || v.startsWith("http:%2f")
    }

    private fun isValidEan13Checksum(code: String): Boolean {
        if (code.length != 13 || !code.all { it.isDigit() }) return false
        val digits = code.map { it - '0' }
        var sum = 0
        for (i in 0 until 12) {
            sum += if (i % 2 == 0) digits[i] else digits[i] * 3
        }
        val check = (10 - (sum % 10)) % 10
        return digits[12] == check
    }

    fun isValidBarcode(barcode: Barcode): Boolean {
        val isSupportedFormat =
            barcode.format == Barcode.FORMAT_EAN_13 || barcode.format == Barcode.FORMAT_EAN_8 ||
                    barcode.format == Barcode.FORMAT_UPC_A || barcode.format == Barcode.FORMAT_UPC_E

        if (!isSupportedFormat) return false

        val raw = barcode.rawValue ?: barcode.displayValue ?: return false
        val code = normalize(raw)

        if (looksLikeUrl(code)) return false

        if (!looksLikeBarcode(code)) return false

        if (barcode.format == Barcode.FORMAT_EAN_13 && !isValidEan13Checksum(code)) return false

        return true
    }
}