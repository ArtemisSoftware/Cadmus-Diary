package com.core.data.di

import com.core.data.repository.MongoDB
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
}
