package com.artemissoftware.cadmusdiary.home.domain.usecases

import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import javax.inject.Inject

class GetAllDiariesUseCase @Inject constructor(private val mongoRepository: MongoRepository) {
    operator fun invoke() = mongoRepository.getAllDiaries()
}
