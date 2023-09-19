package com.artemissoftware.cadmusdiary.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.artemissoftware.cadmusdiary.data.database.entity.ImageToUploadEntity

@Dao
interface ImageToUploadDao {

    @Query("SELECT * FROM imagesToUpload ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToUploadEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImageToUpload(imageToUpload: ImageToUploadEntity)

    @Query("DELETE FROM imagesToUpload WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}
