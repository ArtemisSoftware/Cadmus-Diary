package com.artemissoftware.cadmusdiary.di

import android.content.Context
import androidx.room.Room
import com.artemissoftware.cadmusdiary.core.data.database.ImagesDatabase
import com.artemissoftware.cadmusdiary.util.Constants.IMAGES_DATABASE
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
