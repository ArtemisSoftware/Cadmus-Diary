package com.write.domain.usecases

import com.core.domain.models.Picture
import com.core.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(images: List<Picture>) = withContext(Dispatchers.IO) {
        val sessions = imageRepository.uploadImagesToFirebase(images)
        for ((session, picture) in sessions) {
            imageRepository.insertImage(sessionUri = session, picture = picture)
        }
    }
}
