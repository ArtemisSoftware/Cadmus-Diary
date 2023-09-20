package com.artemissoftware.cadmusdiary.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.artemissoftware.cadmusdiary.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.data.database.entity.ImageToUploadEntity

@Database(
    entities = [
        ImageToUploadEntity::class,
        // ImageToDelete::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    // abstract fun imageToDeleteDao(): ImageToDeleteDao
}
