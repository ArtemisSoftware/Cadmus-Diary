package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(imagesRemotePaths: List<String>) = withContext(Dispatchers.IO) {
        imageRepository.deleteImagesFromFirebase(imagesRemotePaths)

//        val sessions = imageRepository.uploadImagesToFirebase(pictures)
//        for ((session, picture) in sessions) {
//            imageRepository.insertImage(sessionUri = session, picture = picture)
//        }
    }
}
