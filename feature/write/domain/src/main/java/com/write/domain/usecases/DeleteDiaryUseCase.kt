package com.write.domain.usecases

import com.core.domain.RequestState
import com.core.domain.repository.ImageRepository
import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke(
        diaryId: String,
        images: List<String>? = null,
    ) = withContext(Dispatchers.IO) {
        val result = mongoRepository.deleteDiary(id = diaryId)

        if(result is RequestState.Success) {
            if(!images.isNullOrEmpty()) {
                val imagesToDelete = imageRepository.deleteImagesFromFirebase(images)
                // TODO: terminar
                //                            imageToDeleteDao.addImageToDelete(
                //                                ImageToDelete(remoteImagePath = remotePath)
                //                            )
            }
        }

        result
    }
}
