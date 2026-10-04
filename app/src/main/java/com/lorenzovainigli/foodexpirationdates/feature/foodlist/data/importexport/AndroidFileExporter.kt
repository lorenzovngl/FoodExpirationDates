package com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport

import android.content.Context
import com.lorenzovainigli.foodexpirationdates.saveFileToExternalStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import java.io.File

@Singleton
class AndroidFileExporter @Inject constructor(
    @ApplicationContext private val context: Context,
) : FileExporter {

    override fun export(
        source: File,
        fileName: String,
    ) {
        saveFileToExternalStorage(
            context,
            "file://${source.path}",
            fileName,
        )
    }
}