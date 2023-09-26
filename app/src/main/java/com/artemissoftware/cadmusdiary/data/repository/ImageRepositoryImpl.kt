package com.artemissoftware.cadmusdiary.data.repository

import android.util.Log
import androidx.core.net.toUri
import com.artemissoftware.cadmusdiary.data.database.dao.ImageToUploadDao
import com.artemissoftware.cadmusdiary.data.mappers.toEntity
import com.artemissoftware.cadmusdiary.domain.model.Picture
import com.artemissoftware.cadmusdiary.domain.repository.ImageRepository
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

        return suspendCoroutine { continuation ->
            pictures.forEachIndexed { index, picture ->
                val imagePath = storage.child(picture.remotePath)
                imagePath.putFile(picture.image.toUri())
                    .addOnCompleteListener {
                        map[it.result.uploadSessionUri.toString()] = picture

                        if(map.size == pictures.size) {
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

    override suspend fun getImagesFromFirebase(
        remoteImagePaths: List<String>,
//        onImageDownload: (Uri) -> Unit,
//        onImageDownloadFailed: (Exception) -> Unit = {},
//        onReadyToDisplay: () -> Unit = {}
    ): List<String> {
        var urls = mutableListOf<String>()
        var numberOfFailedDownloads = 0
        var downloadsFinished = 0
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
//                                if (/*remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index*/remoteImagePaths.size == urls.size) {
//                                    Log.d("DownloadURL", "Terminei com ${urls.size}")
//                                    continuation.resume(urls)
//                                }
                            }.addOnFailureListener {
                                ++numberOfFailedDownloads
//                            onImageDownloadFailed(it)
                            }
                            .addOnCompleteListener {
                                ++downloadsFinished
                                if (downloadsFinished == remoteImagePaths.size) {
                                    when {
                                        (numberOfFailedDownloads == remoteImagePaths.size) -> {
                                            continuation.resumeWithException(Exception("Failed loading images"))
                                        }

                                        (urls.size + numberOfFailedDownloads == remoteImagePaths.size) -> {
                                            continuation.resume(urls)
                                        }

                                        ((urls.size + numberOfFailedDownloads) == remoteImagePaths.size) -> {
                                            continuation.resume(urls)
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }

    override suspend fun deleteImagesFromFirebase(images: List<String>) {
        val storage = FirebaseStorage.getInstance().reference

        images.forEach { remotePath ->
            storage.child(remotePath).delete()
            // TODO: completar
//                    .addOnFailureListener {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            imageToDeleteDao.addImageToDelete(
//                                ImageToDelete(remoteImagePath = remotePath)
//                            )
//                        }
//                    }
        }
    }

    override suspend fun deleteAllImagesFromFirebase(): List<String> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val imagesDirectory = "images/$userId"
        val storage = FirebaseStorage.getInstance().reference
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
