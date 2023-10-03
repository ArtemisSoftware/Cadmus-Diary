package com.artemissoftware.cadmusdiary.di

import com.artemissoftware.cadmusdiary.core.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.authentication.data.repository.AuthenticationRepositoryImpl
import com.artemissoftware.cadmusdiary.core.data.repository.ImageRepositoryImpl
import com.artemissoftware.cadmusdiary.authentication.domain.repository.AuthenticationRepository
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
    fun provideImageRepository(imageToUploadDao: ImageToUploadDao): ImageRepository {
        return ImageRepositoryImpl(imageToUploadDao = imageToUploadDao)
    }
}
