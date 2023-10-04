package com.artemissoftware.cadmusdiary.write.domain.usecases

import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import kotlinx.coroutines.flow.catch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class GetDiaryUseCase @Inject constructor(
    private val mongoRepository: MongoRepository,
) {

    operator fun invoke(diaryId: String) =
        mongoRepository.getSelectedDiary(diaryId = ObjectId.invoke(diaryId))
            .catch {
                emit(RequestState.Error(Exception("Diary is already deleted.")))
            }
}
