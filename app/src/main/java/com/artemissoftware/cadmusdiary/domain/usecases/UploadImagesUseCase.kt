package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UploadImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(images: List<GalleryImage>) = withContext(Dispatchers.IO) {
        try {
            // TODO: remover galleryImage e ficar só com picture
            val pictures = images.map { image -> Picture(image = image.toString(), remotePath = image.remoteImagePath) }

            val session = imageRepository.uploadImagesToFirebase(pictures)
            session?.let { imageRepository.insertImages(sessionUri = it, pictures = pictures) }
            RequestState.Success(Unit)
        } catch (ex: Exception) {
            RequestState.Error(ex)
        }
    }
}
