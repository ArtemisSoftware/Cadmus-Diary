package com.artemissoftware.cadmusdiary.home.domain.usecases

import com.core.domain.repository.MongoRepository
import java.time.ZonedDateTime
import javax.inject.Inject

class GetFilteredDiariesUseCase @Inject constructor(private val mongoRepository: MongoRepository) {
    operator fun invoke(zonedDateTime: ZonedDateTime) = mongoRepository.getFilteredDiaries(zonedDateTime)
}
