package com.artemissoftware.cadmusdiary.domain.repository

import com.artemissoftware.cadmusdiary.domain.model.Picture

interface ImageRepository {

    fun uploadImagesToFirebase(pictures: List<Picture>)

    suspend fun getImagesFromFirebase(remoteImagePaths: List<String>): List<String>
}
