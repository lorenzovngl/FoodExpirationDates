package com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.lorenzovainigli.foodexpirationdates.model.entity.CSV_HEADER
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.MeasurementUnit
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.util.OperationResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.ByteArrayInputStream
import java.io.File
import java.time.LocalDate
import java.time.ZoneId

class ExpirationDateImportExportManagerTest {

    companion object {
        private const val LEGACY_CSV_HEADER_V1 =
            "FOOD_NAME,EXPIRATION_DATE,OPENING_DATE,TIME_SPAN_DAYS"

        private const val LEGACY_CSV_HEADER_V2 =
            "$LEGACY_CSV_HEADER_V1,QUANTITY"
    }

    @get:Rule
    val temporaryFolder = TemporaryFolder()

    private lateinit var context: Context
    private lateinit var repository: ExpirationDateRepository
    private lateinit var contentResolver: ContentResolver
    private lateinit var uri: Uri
    private lateinit var fileExporter: FileExporter

    private lateinit var manager: ExpirationDateImportExportManager

    @Before
    fun setUp() {
        context = mockk()
        repository = mockk()
        contentResolver = mockk()
        uri = mockk()
        fileExporter = mockk(relaxed = true)

        every {
            context.filesDir
        } returns temporaryFolder.root

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        manager = ExpirationDateImportExportManager(
            context = context,
            repository = repository,
            fileExporter = fileExporter
        )
    }

    // region Import - Error handling

    @Test
    fun `import returns failure when uri is null`() = runTest {
        val result = manager.import(
            contentResolver = contentResolver,
            uri = null,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure when input stream cannot be opened`() = runTest {
        every {
            contentResolver.openInputStream(uri)
        } returns null

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure when reading file throws`() = runTest {
        every {
            contentResolver.openInputStream(uri)
        } throws RuntimeException("Cannot read file")

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure for empty csv`() = runTest {
        mockCsv("")

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure for invalid header`() = runTest {
        mockCsv(
            """
            invalid,header,opening,timeSpan,quantity
            Milk,1700000000000,null,null,1
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure for invalid optional columns`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2,PRICE,COLOR
            Milk,2026-11-25,null,null,2,500,GRAM
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure when numeric value is malformed`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2
            Milk,not_a_timestamp,null,null,1
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure when repository insertion throws`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2
            Milk,1700000000000,null,null,1
            """.trimIndent()
        )

        coEvery {
            repository.addExpirationDate(any())
        } throws RuntimeException("Database error")

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import inserts no rows when one row is invalid`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,null,null,2,500,MILLILITER
            Pasta,2026-11-26,null,null,1,250,GRAM
            Yogurt,2026-11-27,null,null,3,125,INVALID_UNIT
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    // endregion

    // region Import - Backward compatibility

    @Test
    fun `import supports legacy csv without quantity`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V1
            Milk,2026-11-25,null,null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.id == 0 &&
                            it.foodName == "Milk" &&
                            it.expirationDate == dateToEpoch("2026-11-25") &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 1 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import inserts valid expiration date`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2
            Milk,1700000000000,null,null,2
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.id == 0 &&
                            it.foodName == "Milk" &&
                            it.expirationDate == 1700000000000L &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 2 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import inserts all rows`() = runTest {
        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2
            Milk,1700000000000,null,null,1
            Yogurt,1701000000000,null,5,2
            Cheese,1702000000000,1701500000000,null,3
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 3) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import supports missing trailing optional columns`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,null,null,2
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Milk" &&
                            it.quantity == 2 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    // endregion

    // region Import - Optional values

    @Test
    fun `import correctly parses optional values`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Yogurt,1700000000000,1699000000000,5,3,125,GRAM
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.id == 0 &&
                            it.foodName == "Yogurt" &&
                            it.expirationDate == 1700000000000L &&
                            it.openingDate == 1699000000000L &&
                            it.timeSpanDays == 5 &&
                            it.quantity == 3 &&
                            it.contentPerPackage == 125 &&
                            it.measurementUnit == MeasurementUnit.GRAM
                }
            )
        }
    }

    @Test
    fun `import supports blank optional values`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Pasta,2026-11-25,,,,,
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Pasta" &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 1 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import supports case insensitive null values`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Pasta,2026-11-25,NULL,Null,nUlL,NULL,Null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Pasta" &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 1 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import supports null content and measurement unit`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Eggs,2026-11-25,null,null,6,null,null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Eggs" &&
                            it.quantity == 6 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import returns failure for malformed quantity`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,null,null,abc,500,MILLILITER
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    // endregion

    // region Import - Measurement units

    @Test
    fun `import supports all measurement units`() = runTest {
        val csv = buildString {
            appendLine(CSV_HEADER)

            MeasurementUnit.entries.forEach { unit ->
                appendLine(
                    "Food_${unit.name},2026-11-25,null,null,2,100,${unit.name}"
                )
            }
        }

        mockCsv(csv)

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = MeasurementUnit.entries.size) {
            repository.addExpirationDate(any())
        }

        MeasurementUnit.entries.forEach { unit ->
            coVerify(exactly = 1) {
                repository.addExpirationDate(
                    match {
                        it.foodName == "Food_${unit.name}" &&
                                it.expirationDate == dateToEpoch("2026-11-25") &&
                                it.quantity == 2 &&
                                it.contentPerPackage == 100 &&
                                it.measurementUnit == unit
                    }
                )
            }
        }
    }

    @Test
    fun `import returns failure for invalid measurement unit`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,null,null,2,500,INVALID_UNIT
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure for malformed content per package`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,null,null,2,abc,MILLILITER
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    // endregion

    // region Import - Date formats

    @Test
    fun `import correctly parses ISO dates`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-11-25,2026-11-19,5,2,500,MILLILITER
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Milk" &&
                            it.expirationDate == dateToEpoch("2026-11-25") &&
                            it.openingDate == dateToEpoch("2026-11-19") &&
                            it.timeSpanDays == 5 &&
                            it.quantity == 2 &&
                            it.contentPerPackage == 500 &&
                            it.measurementUnit == MeasurementUnit.MILLILITER
                }
            )
        }
    }

    @Test
    fun `import correctly parses ISO dates with null optional values`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Pasta,2027-06-15,null,null,4,null,null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Pasta" &&
                            it.expirationDate == dateToEpoch("2027-06-15") &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 4 &&
                            it.contentPerPackage == null &&
                            it.measurementUnit == null
                }
            )
        }
    }

    @Test
    fun `import supports mixed epoch and ISO formats`() = runTest {
        val epochExpiration = dateToEpoch("2026-12-10")
        val epochOpening = dateToEpoch("2026-11-10")

        mockCsv(
            """
            $LEGACY_CSV_HEADER_V2
            Milk,$epochExpiration,null,null,1
            Yogurt,2026-12-15,2026-11-15,5,2
            Cheese,2026-12-20,$epochOpening,null,3
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 3) {
            repository.addExpirationDate(any())
        }

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Milk" &&
                            it.expirationDate == epochExpiration
                }
            )
        }

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Yogurt" &&
                            it.expirationDate == dateToEpoch("2026-12-15") &&
                            it.openingDate == dateToEpoch("2026-11-15")
                }
            )
        }

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Cheese" &&
                            it.expirationDate == dateToEpoch("2026-12-20") &&
                            it.openingDate == epochOpening
                }
            )
        }
    }

    @Test
    fun `import returns failure for invalid ISO date`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,2026-13-45,null,null,1,null,null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    @Test
    fun `import returns failure for unsupported date format`() = runTest {
        mockCsv(
            """
            $CSV_HEADER
            Milk,25/11/2026,null,null,1,null,null
            """.trimIndent()
        )

        val result = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        verifyNoInsertions()
    }

    // endregion

    // region Export

    @Test
    fun `export creates csv containing header and data`() = runTest {
        val expirationDate = ExpirationDate(
            id = 1,
            foodName = "Milk",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = null,
            timeSpanDays = null,
            quantity = 2,
            contentPerPackage = 500,
            measurementUnit = MeasurementUnit.MILLILITER,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(expirationDate))

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val exportedFile = findExportedCsv()
        val lines = exportedFile.readLines()

        assertEquals(CSV_HEADER, lines[0])

        assertEquals(
            "\"Milk\",2026-11-25,null,null,2,500,MILLILITER",
            lines[1],
        )

        verify(exactly = 1) {
            fileExporter.export(
                source = exportedFile,
                fileName = exportedFile.name,
            )
        }
    }

    @Test
    fun `export creates csv with all entries`() = runTest {
        val items = listOf(
            ExpirationDate(
                id = 1,
                foodName = "Milk",
                expirationDate = dateToEpoch("2026-11-25"),
                openingDate = null,
                timeSpanDays = null,
                quantity = 1,
                contentPerPackage = 1000,
                measurementUnit = MeasurementUnit.MILLILITER,
            ),
            ExpirationDate(
                id = 2,
                foodName = "Cheese",
                expirationDate = dateToEpoch("2026-11-26"),
                openingDate = null,
                timeSpanDays = null,
                quantity = 2,
                contentPerPackage = 250,
                measurementUnit = MeasurementUnit.GRAM,
            ),
            ExpirationDate(
                id = 3,
                foodName = "Eggs",
                expirationDate = dateToEpoch("2026-11-27"),
                openingDate = null,
                timeSpanDays = null,
                quantity = 6,
                contentPerPackage = null,
                measurementUnit = null,
            ),
        )

        coEvery {
            repository.getAll()
        } returns flowOf(items)

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val file = findExportedCsv()
        val lines = file.readLines()

        assertEquals(items.size + 1, lines.size)
        assertEquals(CSV_HEADER, lines[0])

        assertTrue(
            lines.contains(
                "\"Milk\",2026-11-25,null,null,1,1000,MILLILITER"
            )
        )

        assertTrue(
            lines.contains(
                "\"Cheese\",2026-11-26,null,null,2,250,GRAM"
            )
        )

        assertTrue(
            lines.contains(
                "\"Eggs\",2026-11-27,null,null,6,null,null"
            )
        )

        verify(exactly = 1) {
            fileExporter.export(
                source = file,
                fileName = file.name,
            )
        }
    }

    @Test
    fun `export creates csv with only header when repository is empty`() = runTest {
        coEvery {
            repository.getAll()
        } returns flowOf(emptyList())

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val file = findExportedCsv()
        val lines = file.readLines()

        assertEquals(listOf(CSV_HEADER), lines)

        verify(exactly = 1) {
            fileExporter.export(
                source = file,
                fileName = file.name,
            )
        }
    }

    @Test
    fun `export writes ISO dates instead of epoch`() = runTest {
        val entry = ExpirationDate(
            id = 1,
            foodName = "Milk",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = dateToEpoch("2026-11-19"),
            timeSpanDays = 5,
            quantity = 2,
            contentPerPackage = 500,
            measurementUnit = MeasurementUnit.MILLILITER,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val lines = findExportedCsv().readLines()

        assertEquals(CSV_HEADER, lines[0])

        assertEquals(
            "\"Milk\",2026-11-25,2026-11-19,5,2,500,MILLILITER",
            lines[1],
        )
    }

    @Test
    fun `export writes null for missing optional values`() = runTest {
        val entry = ExpirationDate(
            id = 1,
            foodName = "Pasta",
            expirationDate = dateToEpoch("2027-06-15"),
            openingDate = null,
            timeSpanDays = null,
            quantity = 4,
            contentPerPackage = null,
            measurementUnit = null,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val lines = findExportedCsv().readLines()

        assertEquals(
            "\"Pasta\",2027-06-15,null,null,4,null,null",
            lines[1],
        )
    }

    @Test
    fun `export correctly escapes food names containing commas and quotes`() = runTest {
        val entry = ExpirationDate(
            id = 1,
            foodName = "Tomatoes, \"Cherry\"",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = null,
            timeSpanDays = null,
            quantity = 1,
            contentPerPackage = null,
            measurementUnit = null,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val lines = findExportedCsv().readLines()

        assertEquals(
            "\"Tomatoes, \"\"Cherry\"\"\",2026-11-25,null,null,1,null,null",
            lines[1],
        )
    }

    @Test
    fun `export supports all measurement units`() = runTest {
        val items = MeasurementUnit.entries.mapIndexed { index, unit ->
            ExpirationDate(
                id = index + 1,
                foodName = "Food_${unit.name}",
                expirationDate = dateToEpoch("2026-11-25"),
                openingDate = null,
                timeSpanDays = null,
                quantity = 2,
                contentPerPackage = 100,
                measurementUnit = unit,
            )
        }

        coEvery {
            repository.getAll()
        } returns flowOf(items)

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val lines = findExportedCsv().readLines()

        assertEquals(
            MeasurementUnit.entries.size + 1,
            lines.size,
        )

        MeasurementUnit.entries.forEach { unit ->
            assertTrue(
                lines.contains(
                    "\"Food_${unit.name}\",2026-11-25,null,null,2,100,${unit.name}"
                )
            )
        }
    }

    // endregion

    // region Round-trip

    @Test
    fun `exported CSV can be imported preserving all values`() = runTest {
        val original = ExpirationDate(
            id = 1,
            foodName = "Tomatoes, Cherry",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = dateToEpoch("2026-11-19"),
            timeSpanDays = 5,
            quantity = 3,
            contentPerPackage = 250,
            measurementUnit = MeasurementUnit.GRAM,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(original))

        val exportResult = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            exportResult.state,
        )

        mockCsv(findExportedCsv().readText())

        val importResult = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            importResult.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                original.copy(id = 0)
            )
        }
    }

    @Test
    fun `exported CSV preserves missing optional values`() = runTest {
        val original = ExpirationDate(
            id = 1,
            foodName = "Eggs",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = null,
            timeSpanDays = null,
            quantity = 6,
            contentPerPackage = null,
            measurementUnit = null,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(original))

        val exportResult = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            exportResult.state,
        )

        mockCsv(findExportedCsv().readText())

        val importResult = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            importResult.state,
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                original.copy(id = 0)
            )
        }
    }

    @Test
    fun `exported CSV preserves all measurement units`() = runTest {
        val originals = MeasurementUnit.entries.mapIndexed { index, unit ->
            ExpirationDate(
                id = index + 1,
                foodName = "Food_${unit.name}",
                expirationDate = dateToEpoch("2026-11-25"),
                openingDate = null,
                timeSpanDays = null,
                quantity = 3,
                contentPerPackage = 100,
                measurementUnit = unit,
            )
        }

        coEvery {
            repository.getAll()
        } returns flowOf(originals)

        val exportResult = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            exportResult.state,
        )

        mockCsv(findExportedCsv().readText())

        val importResult = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            importResult.state,
        )

        coVerify(exactly = originals.size) {
            repository.addExpirationDate(any())
        }

        originals.forEach { original ->
            coVerify(exactly = 1) {
                repository.addExpirationDate(
                    original.copy(id = 0)
                )
            }
        }
    }

    // endregion

    // region Helpers

    private fun mockCsv(content: String) {
        every {
            contentResolver.openInputStream(uri)
        } answers {
            ByteArrayInputStream(
                content.toByteArray()
            )
        }
    }

    private fun findExportedCsv(): File {
        return requireNotNull(
            temporaryFolder.root
                .listFiles()
                ?.singleOrNull {
                    it.extension == "csv"
                }
        )
    }

    private fun verifyNoInsertions() {
        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    private fun dateToEpoch(date: String): Long =
        LocalDate.parse(date)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

    // endregion
}
