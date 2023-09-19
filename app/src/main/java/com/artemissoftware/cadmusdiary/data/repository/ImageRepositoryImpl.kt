package com.artemissoftware.cadmusdiary.data.repository

import android.net.Uri
import android.util.Log
import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    override suspend fun getImagesFromFirebase(
        remoteImagePaths: List<String>,
//        onImageDownload: (Uri) -> Unit,
//        onImageDownloadFailed: (Exception) -> Unit = {},
//        onReadyToDisplay: () -> Unit = {}
    ): List<String> {
        var urls = mutableListOf<String>()
        val firebaseStorage = FirebaseStorage.getInstance()

        return suspendCoroutine { continuation ->
            if (remoteImagePaths.isNotEmpty()) {
                remoteImagePaths.forEachIndexed { index, remoteImagePath ->
                    if (remoteImagePath.trim().isNotEmpty()) {
                        firebaseStorage.reference.child(remoteImagePath.trim()).downloadUrl
                            .addOnSuccessListener { uri ->
                                Log.d("DownloadURL", "$uri")
                                urls.add(uri.toString())

                                // onImageDownload(it)
                                if (/*remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index*/remoteImagePaths.size == urls.size) {
                                    Log.d("DownloadURL", "Terminei com ${urls.size}")
                                    continuation.resume(urls)
                                }
                            }.addOnFailureListener {
                                continuation.resumeWithException(it)
//                            onImageDownloadFailed(it)
                            }
                    }
                }
            }
        }
    }
}
