package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.model.DiaryImages
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetDiaryImagesUseCase(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(diaryId: String, remoteImagePaths: List<String>) = withContext(Dispatchers.IO) {
        try {
            val result = imageRepository.getImagesFromFirebase(remoteImagePaths)
            RequestState.Success(DiaryImages(id = diaryId, images = result))
        } catch (ex: Exception) {
            RequestState.Error(ex)
        }
    }
}
