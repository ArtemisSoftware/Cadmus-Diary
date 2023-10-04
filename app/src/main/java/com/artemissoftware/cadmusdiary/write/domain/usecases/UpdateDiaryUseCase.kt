package com.artemissoftware.cadmusdiary.write.domain.usecases

import com.artemissoftware.cadmusdiary.core.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateDiaryUseCase @Inject constructor() {

    suspend operator fun invoke(diary: Diary) = withContext(Dispatchers.IO) {
        val result = MongoDB.updateDiary(diary = diary)
        result
    }
}