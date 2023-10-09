package com.artemissoftware.cadmusdiary.home.domain.usecases

import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class GetAllDiariesUseCase @Inject constructor(private val mongoRepository: MongoRepository) {
    @OptIn(FlowPreview::class)
    operator fun invoke() = mongoRepository.getAllDiaries().debounce(2.seconds)
}
