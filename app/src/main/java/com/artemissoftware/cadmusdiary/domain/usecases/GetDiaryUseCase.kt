package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.RequestState
import kotlinx.coroutines.flow.catch
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class GetDiaryUseCase @Inject constructor() {

    operator fun invoke(diaryId: String) =
        MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(diaryId))
            .catch {
                emit(RequestState.Error(Exception("Diary is already deleted.")))
            }
}
