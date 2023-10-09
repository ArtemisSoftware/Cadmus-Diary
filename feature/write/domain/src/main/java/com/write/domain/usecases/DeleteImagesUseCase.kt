package com.write.domain.usecases

import com.core.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(imagesRemotePaths: List<String>) = withContext(Dispatchers.IO) {
        val imagesToDelete = imageRepository.deleteImagesFromFirebase(imagesRemotePaths)
        // TODO: terminar
//                            imageToDeleteDao.addImageToDelete(
//                                ImageToDelete(remoteImagePath = remotePath)
//                            )
    }
}
