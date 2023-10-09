package com.write.domain.usecases

import com.core.domain.RequestState
import com.core.domain.repository.ImageRepository
import com.core.domain.repository.MongoRepository
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
