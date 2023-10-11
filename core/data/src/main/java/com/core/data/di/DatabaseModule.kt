package com.core.data.di

import android.content.Context
import androidx.room.Room
import com.core.data.database.ImagesDatabase
import com.core.data.util.DatabaseConstants.IMAGES_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): ImagesDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ImagesDatabase::class.java,
            name = IMAGES_DATABASE,
        ).build()
    }

    @Singleton
    @Provides
    fun provideImageToUploadDao(database: ImagesDatabase) = database.imageToUploadDao()

//    @Singleton
//    @Provides
//    fun provideSecondDao(database: ImagesDatabase) = database.imageToDeleteDao()
}
