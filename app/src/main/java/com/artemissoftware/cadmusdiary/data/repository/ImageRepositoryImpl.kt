package com.artemissoftware.cadmusdiary.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.artemissoftware.cadmusdiary.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.data.mappers.toEntity
import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ImageRepositoryImpl(
    private val imageToUploadDao: ImageToUploadDao
) : ImageRepository {
    override suspend fun uploadImagesToFirebase(pictures: List<Picture>): String? {
        return suspendCoroutine { continuation ->

            val storage = FirebaseStorage.getInstance().reference
            pictures.forEach { picture ->
                val imagePath = storage.child(picture.remotePath)
                imagePath.putFile(picture.image.toUri())
                    .addOnProgressListener {
                        val sessionUri = it.uploadSessionUri
                        continuation.resume(sessionUri?.toString())
                    }
            }
        }
    }

    override suspend fun insertImages(sessionUri: String, pictures: List<Picture>){
        val session = sessionUri.toUri()
        val entities = pictures.map { picture -> picture.toEntity(sessionUri = session) }
        imageToUploadDao.addImageToUpload(imageToUpload = entities)
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
