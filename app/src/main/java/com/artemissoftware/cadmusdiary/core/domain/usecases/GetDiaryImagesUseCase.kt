package com.artemissoftware.cadmusdiary.core.domain.usecases

import com.artemissoftware.cadmusdiary.core.domain.repository.ImageRepository
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.artemissoftware.cadmusdiary.core.domain.models.DiaryImages
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetDiaryImagesUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    // TODO: fazer lógica para ober uma imagem de cada vez para ter um ui mais bonito
    operator fun invoke(diaryId: String, remoteImagePaths: List<String>) = flow {
        try {
            val result = imageRepository.getImagesFromFirebase(remoteImagePaths)
            if(result.numberOfErrors > 0) {
                emit(RequestState.Error(Throwable("Failed to load " + result.numberOfErrors + " images")))
            }
            emit(RequestState.Success(DiaryImages(id = diaryId, images = result.urls)))
        } catch (ex: Exception) {
            emit(RequestState.Error(ex))
        }
    }.flowOn(Dispatchers.IO)
}
