package com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.lorenzovainigli.foodexpirationdates.model.entity.CSV_HEADER
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
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

        manager = ExpirationDateImportExportManager(
            context = context,
            repository = repository,
            fileExporter = fileExporter
        )
    }

    // region Import

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

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure when input stream cannot be opened`() = runTest {
        every {
            contentResolver.openInputStream(uri)
        } returns null

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure when reading file throws`() = runTest {
        every {
            contentResolver.openInputStream(uri)
        } throws RuntimeException("Cannot read file")

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure for empty csv`() = runTest {
        mockCsv("")

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure for invalid header`() = runTest {
        val csv = """
            invalid,header,opening,timeSpan,quantity
            Milk,1700000000000,null,null,1
        """.trimIndent()

        mockCsv(csv)

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import inserts valid expiration date`() = runTest {
        val csv = """
            $CSV_HEADER
            Milk,1700000000000,null,null,2
        """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

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
                            it.quantity == 2
                }
            )
        }
    }

    @Test
    fun `import correctly parses optional values`() = runTest {
        val csv = """
            $CSV_HEADER
            Yogurt,1700000000000,1699000000000,5,3
        """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify {
            repository.addExpirationDate(
                match {
                    it.foodName == "Yogurt" &&
                            it.expirationDate == 1700000000000L &&
                            it.openingDate == 1699000000000L &&
                            it.timeSpanDays == 5 &&
                            it.quantity == 3
                }
            )
        }
    }

    @Test
    fun `import inserts all rows`() = runTest {
        val csv = """
            $CSV_HEADER
            Milk,1700000000000,null,null,1
            Yogurt,1701000000000,null,5,2
            Cheese,1702000000000,1701500000000,null,3
        """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        coVerify(exactly = 3) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure when numeric value is malformed`() = runTest {
        val csv = """
            $CSV_HEADER
            Milk,not_a_timestamp,null,null,1
        """.trimIndent()

        mockCsv(csv)

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )
    }

    @Test
    fun `import returns failure when repository insertion throws`() = runTest {
        val csv = """
            $CSV_HEADER
            Milk,1700000000000,null,null,1
        """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } throws RuntimeException("Database error")

        val result = manager.import(
            contentResolver = contentResolver,
            uri = uri,
        )

        assertEquals(
            OperationResult.State.FAILURE,
            result.state,
        )
    }

    // endregion

    // region Export

    @Test
    fun `export creates csv containing header and data`() = runTest {
        val expirationDate = ExpirationDate(
            id = 1,
            foodName = "Milk",
            expirationDate = 1700000000000L,
            openingDate = null,
            timeSpanDays = null,
            quantity = 2,
        )

        coEvery {
            repository.getAll()
        } returns flowOf(
            listOf(expirationDate)
        )

        val result = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            result.state,
        )

        val exportedFile = temporaryFolder.root
            .listFiles()
            ?.singleOrNull { it.extension == "csv" }

        assertTrue(exportedFile != null)

        val content = exportedFile!!.readText()

        assertTrue(content.contains(CSV_HEADER))
        assertTrue(content.contains("Milk"))

        verify {
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
                expirationDate = 1700000000000L,
                openingDate = null,
                timeSpanDays = null,
                quantity = 1,
            ),
            ExpirationDate(
                id = 2,
                foodName = "Cheese",
                expirationDate = 1701000000000L,
                openingDate = null,
                timeSpanDays = null,
                quantity = 2,
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
        val content = file.readText()

        assertTrue(content.contains("Milk"))
        assertTrue(content.contains("Cheese"))

        verify {
            fileExporter.export(
                source = file,
                fileName = file.name,
            )
        }
    }

    // endregion

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


    @Test
    fun `import correctly parses ISO dates`() = runTest {
        val csv = """
        $CSV_HEADER
        Milk,2026-11-25,2026-11-19,5,2
    """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(contentResolver, uri)

        assertEquals(OperationResult.State.SUCCESS, result.state)

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Milk" &&
                            it.expirationDate == dateToEpoch("2026-11-25") &&
                            it.openingDate == dateToEpoch("2026-11-19") &&
                            it.timeSpanDays == 5 &&
                            it.quantity == 2
                }
            )
        }
    }

    @Test
    fun `import correctly parses ISO dates with null optional values`() = runTest {
        val csv = """
        $CSV_HEADER
        Pasta,2027-06-15,null,null,4
    """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(contentResolver, uri)

        assertEquals(OperationResult.State.SUCCESS, result.state)

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == "Pasta" &&
                            it.expirationDate == dateToEpoch("2027-06-15") &&
                            it.openingDate == null &&
                            it.timeSpanDays == null &&
                            it.quantity == 4
                }
            )
        }
    }

    @Test
    fun `import supports mixed epoch and ISO formats`() = runTest {
        val epochExpiration = dateToEpoch("2026-12-10")
        val epochOpening = dateToEpoch("2026-11-10")

        val csv = """
        $CSV_HEADER
        Milk,$epochExpiration,null,null,1
        Yogurt,2026-12-15,2026-11-15,5,2
        Cheese,2026-12-20,$epochOpening,null,3
    """.trimIndent()

        mockCsv(csv)

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val result = manager.import(contentResolver, uri)

        assertEquals(OperationResult.State.SUCCESS, result.state)

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
        val csv = """
        $CSV_HEADER
        Milk,2026-13-45,null,null,1
    """.trimIndent()

        mockCsv(csv)

        val result = manager.import(contentResolver, uri)

        assertEquals(OperationResult.State.FAILURE, result.state)

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
        }
    }

    @Test
    fun `import returns failure for unsupported date format`() = runTest {
        val csv = """
        $CSV_HEADER
        Milk,25/11/2026,null,null,1
    """.trimIndent()

        mockCsv(csv)

        val result = manager.import(contentResolver, uri)

        assertEquals(OperationResult.State.FAILURE, result.state)

        coVerify(exactly = 0) {
            repository.addExpirationDate(any())
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
            quantity = 2
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(OperationResult.State.SUCCESS, result.state)

        val lines = findExportedCsv().readLines()

        assertEquals(CSV_HEADER, lines[0])
        assertEquals(
            "\"Milk\",2026-11-25,2026-11-19,5,2",
            lines[1]
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
            quantity = 4
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(OperationResult.State.SUCCESS, result.state)

        val lines = findExportedCsv().readLines()

        assertEquals(
            "\"Pasta\",2027-06-15,null,null,4",
            lines[1]
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
            quantity = 1
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(entry))

        val result = manager.export()

        assertEquals(OperationResult.State.SUCCESS, result.state)

        val lines = findExportedCsv().readLines()

        assertEquals(
            "\"Tomatoes, \"\"Cherry\"\"\",2026-11-25,null,null,1",
            lines[1]
        )
    }


    @Test
    fun `exported CSV can be imported preserving all values`() = runTest {
        val original = ExpirationDate(
            id = 1,
            foodName = "Tomatoes, Cherry",
            expirationDate = dateToEpoch("2026-11-25"),
            openingDate = dateToEpoch("2026-11-19"),
            timeSpanDays = 5,
            quantity = 3
        )

        coEvery {
            repository.getAll()
        } returns flowOf(listOf(original))

        coEvery {
            repository.addExpirationDate(any())
        } returns Unit

        val exportResult = manager.export()

        assertEquals(
            OperationResult.State.SUCCESS,
            exportResult.state
        )

        mockCsv(findExportedCsv().readText())

        val importResult = manager.import(contentResolver, uri)

        assertEquals(
            OperationResult.State.SUCCESS,
            importResult.state
        )

        coVerify(exactly = 1) {
            repository.addExpirationDate(
                match {
                    it.foodName == original.foodName &&
                            it.expirationDate == original.expirationDate &&
                            it.openingDate == original.openingDate &&
                            it.timeSpanDays == original.timeSpanDays &&
                            it.quantity == original.quantity
                }
            )
        }
    }

    private fun dateToEpoch(date: String): Long =
        LocalDate.parse(date)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
}