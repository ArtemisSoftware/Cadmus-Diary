package com.artemissoftware.cadmusdiary.write.domain.usecases

import com.core.domain.repository.MongoRepository
import com.core.domain.repository.ImageRepository
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

        if(result is com.core.domain.RequestState.Success) {
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
