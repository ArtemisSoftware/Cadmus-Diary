package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(private val imageRepository: ImageRepository) {

    suspend operator fun invoke(
        diaryId: String,
        images: RealmList<String>? = null,
    ) = withContext(Dispatchers.IO) {
        val result = MongoDB.deleteDiary(id = ObjectId.invoke(diaryId))

        if(result is RequestState.Success) {
            imageRepository.deleteImagesFromFirebase(images)
        }

        result
    }
}
