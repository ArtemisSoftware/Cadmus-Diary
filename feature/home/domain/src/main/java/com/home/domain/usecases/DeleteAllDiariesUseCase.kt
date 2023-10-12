package com.home.domain.usecases

import com.core.domain.repository.ImageRepository
import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteAllDiariesUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val imagesToDelete = imageRepository.deleteAllImagesFromFirebase()
        imageRepository.deleteImagesFormDatabase(imagesToDelete)

        val result = mongoRepository.deleteAllDiaries()

        result
    }
}
