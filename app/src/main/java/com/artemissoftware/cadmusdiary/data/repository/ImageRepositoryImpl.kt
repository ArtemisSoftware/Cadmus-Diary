package com.artemissoftware.cadmusdiary.data.repository

import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage

class ImageRepositoryImpl() : ImageRepository {
    override fun uploadImagesToFirebase(pictures: List<Picture>) {
        val storage = FirebaseStorage.getInstance().reference
        pictures.forEach { picture ->
            val imagePath = storage.child(picture.remotePath)
            imagePath.putFile(picture.image)
//                .addOnProgressListener {
//                    val sessionUri = it.uploadSessionUri
//                    if (sessionUri != null) {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            imageToUploadDao.addImageToUpload(
//                                ImageToUpload(
//                                    remoteImagePath = galleryImage.remoteImagePath,
//                                    imageUri = galleryImage.image.toString(),
//                                    sessionUri = sessionUri.toString()
//                                )
//                            )
//                        }
//                    }
//                }
        }
    }
}
