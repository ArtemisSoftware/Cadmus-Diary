package com.artemissoftware.cadmusdiary.write.domain.usecases

import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.artemissoftware.cadmusdiary.core.domain.repository.ImageRepository
import io.realm.kotlin.types.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

class DeleteDiaryUseCase @Inject constructor(
    private val imageRepository: ImageRepository,
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke(
        diaryId: String,
        images: RealmList<String>? = null,
    ) = withContext(Dispatchers.IO) {
        val result = mongoRepository.deleteDiary(id = ObjectId.invoke(diaryId))

        if(result is RequestState.Success) {
            images?.let {
                val imagesToDelete = imageRepository.deleteImagesFromFirebase(it)
                // TODO: terminar
//                            imageToDeleteDao.addImageToDelete(
//                                ImageToDelete(remoteImagePath = remotePath)
//                            )
            }
        }

        result
    }
}
