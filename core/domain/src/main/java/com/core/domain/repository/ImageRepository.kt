package com.core.domain.repository

import com.core.domain.models.ImageResult
import com.core.domain.models.Picture

interface ImageRepository {

    suspend fun uploadImagesToFirebase(pictures: List<Picture>): HashMap<String, Picture>

    suspend fun getImagesFromFirebase(remoteImagePaths: List<String>): ImageResult

    suspend fun insertImage(sessionUri: String, picture: Picture)

    suspend fun deleteImagesFromFirebase(images: List<String>): List<String>

    suspend fun deleteAllImagesFromFirebase(): List<String>

    fun extractImagePath(fullImageUrl: String): String

    fun getRemoteImagePath(imageUri: String, imageType: String): String
}
