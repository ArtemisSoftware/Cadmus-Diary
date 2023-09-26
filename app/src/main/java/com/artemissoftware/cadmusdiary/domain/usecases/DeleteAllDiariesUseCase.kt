package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteAllDiariesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val imagesToDelete = imageRepository.deleteAllImagesFromFirebase()
        // TODO: adicionar aqui
//        imageToDeleteDao.addImageToDelete(
//            ImageToDelete(
//                remoteImagePath = imagePath
//            )
//        )

        val result = MongoDB.deleteAllDiaries()

        result
    }
}
