package com.artemissoftware.cadmusdiary.write.domain.usecases

import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import com.artemissoftware.cadmusdiary.core.domain.models.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke(diary: Diary) = withContext(Dispatchers.IO) {
        val result = mongoRepository.updateDiary(diary = diary)
        result
    }
}
