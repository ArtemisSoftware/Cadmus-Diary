package com.write.domain.usecases

import com.core.domain.repository.ImageRepository
import javax.inject.Inject

class GetRemoteImagePathUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    operator fun invoke(imageUri: String, imageType: String) = imageRepository.getRemoteImagePath(imageUri = imageUri, imageType = imageType)
}
