package com.artemissoftware.cadmusdiary.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artemissoftware.cadmusdiary.data.database.entity.ImageToUploadEntity

@Dao
interface ImageToUploadDao {

    @Query("SELECT * FROM imageToUpload ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUploadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpload(imageToUpload: ImageToUploadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpload(imageToUpload: List<ImageToUploadEntity>)

    @Query("DELETE FROM imageToUpload WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}
