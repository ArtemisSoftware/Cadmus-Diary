package com.core.data.di

import com.core.data.database.dao.ImageToDeleteDao
import com.core.data.database.dao.ImageToUploadDao
import com.core.data.repository.ImageRepositoryImpl
import com.core.data.repository.MongoDB
import com.core.domain.repository.ImageRepository
import com.core.domain.repository.MongoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMongoRepository(): MongoRepository {
        return MongoDB
    }

    @Provides
    @Singleton
    fun provideImageRepository(imageToUploadDao: ImageToUploadDao, imageToDeleteDao: ImageToDeleteDao): ImageRepository {
        return ImageRepositoryImpl(imageToUploadDao = imageToUploadDao, imageToDeleteDao = imageToDeleteDao)
    }
}
