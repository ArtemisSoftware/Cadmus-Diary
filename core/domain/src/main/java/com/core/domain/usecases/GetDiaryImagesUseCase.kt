package com.core.domain.usecases

import com.core.domain.models.DiaryImages
import com.core.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDiaryImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    operator fun invoke(diaryId: String, remoteImagePaths: List<String>) = flow {
        try {
            val result = imageRepository.getImagesFromFirebase(remoteImagePaths)
            if(result.numberOfErrors > 0) {
                emit(com.core.domain.RequestState.Error(Throwable("Failed to load " + result.numberOfErrors + " images")))
            }
            emit(com.core.domain.RequestState.Success(DiaryImages(id = diaryId, images = result.urls)))
        } catch (ex: Exception) {
            emit(com.core.domain.RequestState.Error(ex))
        }
    }.flowOn(Dispatchers.IO)
}
