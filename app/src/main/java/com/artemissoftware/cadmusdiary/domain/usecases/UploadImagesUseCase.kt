package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(images: List<GalleryImage>) = withContext(Dispatchers.IO) {
        val pictures = images.map { image ->
            Picture(
                image = image.image.toString(),
                remotePath = image.remoteImagePath,
            )
        }

        val sessions = imageRepository.uploadImagesToFirebase(pictures)
        for ((session, picture) in sessions) {
            imageRepository.insertImage(sessionUri = session, picture = picture)
        }
    }
}
