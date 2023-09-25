package com.artemissoftware.cadmusdiary.domain.repository

import com.artemissoftware.cadmusdiary.domain.model.Picture

interface ImageRepository {

    suspend fun uploadImagesToFirebase(pictures: List<Picture>): HashMap<String, Picture>

    suspend fun getImagesFromFirebase(remoteImagePaths: List<String>): List<String>

    suspend fun insertImage(sessionUri: String, picture: Picture)

    suspend fun deleteImagesFromFirebase(images: List<String>)
}
