package com.write.domain.usecases

import com.core.domain.repository.ImageRepository
import javax.inject.Inject

class GetRemoteImagePathFromUrlUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    operator fun invoke(fullImageUrl: String) = imageRepository.extractImagePath(fullImageUrl)
}
