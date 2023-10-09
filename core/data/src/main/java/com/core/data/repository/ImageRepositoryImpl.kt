package com.core.data.repository

import androidx.core.net.toUri
import com.core.data.database.dao.ImageToUploadDao
import com.core.data.mappers.toEntity
import com.core.domain.models.ImageResult
import com.core.domain.models.Picture
import com.core.domain.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ImageRepositoryImpl(
    private val imageToUploadDao: ImageToUploadDao,
) : ImageRepository {

    override suspend fun uploadImagesToFirebase(pictures: List<Picture>): HashMap<String, Picture> {
        val storage = FirebaseStorage.getInstance().reference
        val map = HashMap<String, Picture>()
        var uploadsFinished = 0

        return suspendCoroutine { continuation ->
            pictures.forEachIndexed { index, picture ->
                val imagePath = storage.child(picture.remotePath)
                imagePath.putFile(picture.image.toUri())
                    .addOnSuccessListener {
                        map[it.uploadSessionUri.toString()] = picture
                    }
                    .addOnCompleteListener {
                        ++uploadsFinished

                        if(uploadsFinished == pictures.size) {
                            continuation.resume(map)
                        }
                    }
            }
        }
    }

    override suspend fun insertImage(sessionUri: String, picture: Picture) {
        val session = sessionUri.toUri()
        val entities = picture.toEntity(sessionUri = session)
        imageToUploadDao.addImageToUpload(imageToUpload = entities)
    }

    override suspend fun getImagesFromFirebase(remoteImagePaths: List<String>): ImageResult {
        val urls = mutableListOf<String>()
        var numberOfFailedDownloads = 0
        var downloadsFinished = 0
        val firebaseStorage = FirebaseStorage.getInstance()

        return suspendCoroutine { continuation ->
            if (remoteImagePaths.isNotEmpty()) {
                remoteImagePaths.forEach { remoteImagePath ->
                    if (remoteImagePath.trim().isNotEmpty()) {
                        firebaseStorage
                            .reference
                            .child(remoteImagePath.trim())
                            .downloadUrl
                            .addOnSuccessListener { uri ->
                                urls.add(uri.toString())
                            }.addOnFailureListener {
                                ++numberOfFailedDownloads
                            }
                            .addOnCompleteListener {
                                ++downloadsFinished
                                if (downloadsFinished == remoteImagePaths.size) {
                                    continuation.resume(ImageResult(urls = urls, numberOfErrors = numberOfFailedDownloads))
                                }
                            }
                    }
                }
            }
        }
    }

    override suspend fun deleteImagesFromFirebase(images: List<String>): List<String> {
        val storage = FirebaseStorage.getInstance().reference
        val toDelete = mutableListOf<String>()
        var deletesFinished = 0
        return suspendCoroutine { continuation ->
            images.forEach { remotePath ->
                storage.child(remotePath).delete()
                    .addOnFailureListener {
                        toDelete.add(remotePath)
                    }
                    .addOnCompleteListener {
                        ++deletesFinished

                        if (deletesFinished == images.size) {
                            continuation.resume(toDelete)
                        }
                    }
            }
        }
    }

    override suspend fun deleteAllImagesFromFirebase(): List<String> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val storage = FirebaseStorage.getInstance().reference
        val imagesDirectory = "images/$userId"
        val toDelete = mutableListOf<String>()
        var numberOfSuccess = 0
        var deletesFinished = 0
        return suspendCoroutine { continuation ->
            storage.child(imagesDirectory)
                .listAll()
                .addOnSuccessListener { directoryList ->
                    directoryList.items.forEach { ref ->
                        val imagePath = "images/$userId/${ref.name}"
                        storage
                            .child(imagePath)
                            .delete()
                            .addOnFailureListener {
                                toDelete.add(imagePath)
                            }
                            .addOnSuccessListener {
                                ++numberOfSuccess
                            }
                            .addOnCompleteListener {
                                ++deletesFinished

                                if (deletesFinished == directoryList.items.size) {
                                    continuation.resume(toDelete)
                                }
                            }
                    }
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}
