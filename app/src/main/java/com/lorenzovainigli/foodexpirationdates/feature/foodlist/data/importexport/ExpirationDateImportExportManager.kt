package com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.lorenzovainigli.foodexpirationdates.model.entity.CSV_HEADER
import com.lorenzovainigli.foodexpirationdates.model.entity.EXPIRATION_DATE
import com.lorenzovainigli.foodexpirationdates.model.entity.EXPIRATION_DATE_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.ExpirationDate
import com.lorenzovainigli.foodexpirationdates.model.entity.FOOD_NAME
import com.lorenzovainigli.foodexpirationdates.model.entity.FOOD_NAME_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.OPENING_DATE
import com.lorenzovainigli.foodexpirationdates.model.entity.OPENING_DATE_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.QUANTITY_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.TIME_SPAN_DAYS
import com.lorenzovainigli.foodexpirationdates.model.entity.TIME_SPAN_DAYS_INDEX
import com.lorenzovainigli.foodexpirationdates.model.entity.toCSV
import com.lorenzovainigli.foodexpirationdates.model.repository.ExpirationDateRepository
import com.lorenzovainigli.foodexpirationdates.util.OperationResult
import com.opencsv.CSVReader
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpirationDateImportExportManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ExpirationDateRepository,
    private val fileExporter: FileExporter,
) {

    suspend fun export(): OperationResult {
        return try {
            val timeStamp = SimpleDateFormat(
                "yyyyMMdd_HHmmss",
                Locale.getDefault()
            ).format(Date())

            val fileName = "fed_data_$timeStamp.csv"
            val file = File(context.filesDir, fileName)

            withContext(Dispatchers.IO) {
                FileWriter(file).use { writer ->
                    writer.appendLine(CSV_HEADER)

                    repository.getAll()
                        .first()
                        .forEach { entry ->
                            writer.appendLine(entry.toCSV())
                        }
                }
            }

            fileExporter.export(
                source = file,
                fileName = file.name,
            )

            OperationResult(
                OperationResult.State.SUCCESS,
                "Data exported correctly",
            )
        } catch (e: IOException) {
            OperationResult(
                OperationResult.State.FAILURE,
                "Error exporting data",
            )
        }
    }

    suspend fun import(
        contentResolver: ContentResolver,
        uri: Uri?,
    ): OperationResult {
        if (uri == null) {
            return OperationResult(
                OperationResult.State.FAILURE,
                "File not found",
            )
        }

        val csvData = try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                CSVReader(InputStreamReader(inputStream)).use { reader ->
                    reader.readAll()
                }
            } ?: return OperationResult(
                OperationResult.State.FAILURE,
                "Error reading file",
            )
        } catch (e: Exception) {
            return OperationResult(
                OperationResult.State.FAILURE,
                "Error reading file",
            )
        }

        if (!validateCsv(csvData)) {
            return OperationResult(
                OperationResult.State.FAILURE,
                "File not valid",
            )
        }

        return try {
            csvData
                .drop(1)
                .map(::parseRow)
                .forEach { expirationDate ->
                    repository.addExpirationDate(expirationDate)
                }

            OperationResult(
                OperationResult.State.SUCCESS,
                "Data imported correctly",
            )
        } catch (e: Exception) {
            OperationResult(
                OperationResult.State.FAILURE,
                "Error inserting data",
            )
        }
    }

    private fun parseRow(row: Array<String>): ExpirationDate {
        return ExpirationDate(
            id = 0,
            foodName = row[FOOD_NAME_INDEX],
            expirationDate = row[EXPIRATION_DATE_INDEX].toLong(),
            openingDate = row[OPENING_DATE_INDEX]
                .takeUnless { it == "null" }
                ?.toLong(),
            timeSpanDays = row[TIME_SPAN_DAYS_INDEX]
                .takeUnless { it == "null" }
                ?.toInt(),
            quantity = row[QUANTITY_INDEX].toInt(),
        )
    }

    private fun validateCsv(
        csvData: List<Array<String>>
    ): Boolean {
        if (csvData.isEmpty()) {
            return false
        }

        val header = csvData.first()

        return header.size > TIME_SPAN_DAYS_INDEX &&
                header[FOOD_NAME_INDEX] == FOOD_NAME &&
                header[EXPIRATION_DATE_INDEX] == EXPIRATION_DATE &&
                header[OPENING_DATE_INDEX] == OPENING_DATE &&
                header[TIME_SPAN_DAYS_INDEX] == TIME_SPAN_DAYS
    }
}