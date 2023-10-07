package com.artemissoftware.cadmusdiary.di

import com.artemissoftware.cadmusdiary.authentication.data.repository.AuthenticationRepositoryImpl
import com.artemissoftware.cadmusdiary.authentication.domain.repository.AuthenticationRepository
import com.core.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.core.data.repository.ImageRepositoryImpl
import com.artemissoftware.cadmusdiary.core.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import com.artemissoftware.cadmusdiary.core.domain.repository.ImageRepository
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
    fun provideAuthenticationRepository(): AuthenticationRepository {
        return AuthenticationRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideImageRepository(imageToUploadDao: com.core.data.database.dao.ImageToUploadDao): ImageRepository {
        return ImageRepositoryImpl(imageToUploadDao = imageToUploadDao)
    }

    @Provides
    @Singleton
    fun provideMongoRepository(): MongoRepository {
        return MongoDB
    }
}
