package com.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.core.data.database.dao.ImageToDeleteDao
import com.core.data.database.dao.ImageToUploadDao
import com.core.data.database.entity.ImageToDeleteEntity
import com.core.data.database.entity.ImageToUploadEntity

@Database(
    entities = [
        ImageToUploadEntity::class,
        ImageToDeleteEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class ImagesDatabase : RoomDatabase() {
    abstract fun imageToUploadDao(): ImageToUploadDao
    abstract fun imageToDeleteDao(): ImageToDeleteDao
}
