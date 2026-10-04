package com.lorenzovainigli.foodexpirationdates.di

import com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport.AndroidFileExporter
import com.lorenzovainigli.foodexpirationdates.feature.foodlist.data.importexport.FileExporter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FileExporterModule {

    @Binds
    @Singleton
    abstract fun bindFileExporter(
        androidFileExporter: AndroidFileExporter,
    ): FileExporter
}