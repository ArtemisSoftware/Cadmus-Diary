package com.artemissoftware.cadmusdiary.domain.repository

import com.artemissoftware.cadmusdiary.domain.model.Picture

interface ImageRepository {

    suspend fun uploadImagesToFirebase(pictures: List<Picture>): String?

    suspend fun getImagesFromFirebase(remoteImagePaths: List<String>): List<String>

    suspend fun insertImages(sessionUri: String, pictures: List<Picture>)
}
