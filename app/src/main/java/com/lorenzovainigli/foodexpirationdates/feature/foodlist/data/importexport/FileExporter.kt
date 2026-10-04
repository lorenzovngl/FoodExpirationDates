package com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport

import java.io.File

interface FileExporter {
    fun export(source: File, fileName: String)
}