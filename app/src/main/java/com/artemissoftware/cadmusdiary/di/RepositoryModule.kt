package com.artemissoftware.cadmusdiary.di

import com.artemissoftware.cadmusdiary.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.data.repository.ImageRepositoryImpl
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideImageRepository(imageToUploadDao: ImageToUploadDao): ImageRepository {
        return ImageRepositoryImpl(imageToUploadDao = imageToUploadDao)
    }
}