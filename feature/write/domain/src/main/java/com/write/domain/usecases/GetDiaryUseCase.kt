package com.write.domain.usecases

import com.core.domain.RequestState
import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GetDiaryUseCase @Inject constructor(
    private val mongoRepository: MongoRepository,
) {

    operator fun invoke(diaryId: String) =
        mongoRepository.getSelectedDiary(diaryId = diaryId)
            .catch {
                emit(RequestState.Error(Exception("Diary is already deleted.")))
            }
}
