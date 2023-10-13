package com.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.core.data.database.entity.ImageToDeleteEntity

@Dao
interface ImageToDeleteDao {

    @Query("SELECT * FROM imagetodelete ORDER BY id ASC")
    suspend fun getAllImages(): List<ImageToDeleteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addImagesToDelete(imagesToDelete: List<ImageToDeleteEntity>)

    @Query("DELETE FROM imagetodelete WHERE id=:imageId")
    suspend fun cleanupImage(imageId: Int)
}
