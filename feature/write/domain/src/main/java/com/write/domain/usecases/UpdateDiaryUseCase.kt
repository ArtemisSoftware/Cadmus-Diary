package com.write.domain.usecases

import com.core.domain.models.JournalEntry
import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor(
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke(journalEntry: JournalEntry) = withContext(Dispatchers.IO) {
        val result = mongoRepository.updateDiary(journalEntry = journalEntry)
        result
    }
}
