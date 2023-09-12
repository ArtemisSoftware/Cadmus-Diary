package com.artemissoftware.cadmusdiary.presentation.screens.write

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.model.Diary
import com.artemissoftware.cadmusdiary.domain.model.Mood
import com.artemissoftware.cadmusdiary.navigation.Screen.Companion.WRITE_SCREEN_ARGUMENT_KEY
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

// @HiltViewModel
class WriteViewModel /*@Inject*/ constructor(
    private val savedStateHandle: SavedStateHandle,
//    private val imageToUploadDao: ImageToUploadDao,
//    private val imageToDeleteDao: ImageToDeleteDao
) : UiEventViewModel() {

    private val _state = MutableStateFlow(WriteState())
    val state: StateFlow<WriteState> = _state.asStateFlow()
//    val galleryState = GalleryState()

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() = with(_state) {
        update {
            it.copy(
                selectedDiaryId = savedStateHandle.get<String>(
                    key = WRITE_SCREEN_ARGUMENT_KEY,
                ),
            )
        }
    }

    fun onTriggerEvent(event: WriteEvents) {
        when (event) {
            WriteEvents.PopBackStack -> {
                popBackStack()
            }
            is WriteEvents.SetDescription -> {
                setDescription(description = event.description)
            }
            is WriteEvents.SetTitle -> {
                setTitle(title = event.title)
            }
        }
    }

    private fun fetchSelectedDiary() {
        _state.value.selectedDiaryId?.let { selectedDiaryId ->
            viewModelScope.launch {
                // TODO: GetDiaryUseCase

                MongoDB.getSelectedDiary(diaryId = ObjectId.invoke(selectedDiaryId))
                    .catch {
                        emit(RequestState.Error(Exception("Diary is already deleted.")))
                    }
                    .collect { request ->

                        when (request) {
                            is RequestState.Success -> {
                                setSelectedDiary(request.data)

//                            fetchImagesFromFirebase(
//                                remoteImagePaths = diary.data.images,
//                                onImageDownload = { downloadedImage ->
//                                    galleryState.addImage(
//                                        GalleryImage(
//                                            image = downloadedImage,
//                                            remoteImagePath = extractImagePath(
//                                                fullImageUrl = downloadedImage.toString()
//                                            ),
//                                        )
//                                    )
//                                }
//                            )
                            }
                            else -> Unit
                        }
                    }
            }
        }
    }

    private fun setSelectedDiary(diary: Diary) = with(_state) {
        update {
            it.copy(
                selectedDiary = diary,
                title = diary.title,
                description = diary.description,
                mood = Mood.valueOf(diary.mood),
            )
        }
    }

//    private fun setSelectedDiary(diary: Diary) = with(_state) {
//        update {
//            it.copy(selectedDiary = diary)
//        }
//    }

    private fun setTitle(title: String) = with(_state) {
        update {
            it.copy(title = title)
        }
    }

    private fun setDescription(description: String) = with(_state) {
        update {
            it.copy(description = description)
        }
    }

    private fun setMood(mood: Mood) = with(_state) {
        update {
            it.copy(mood = mood)
        }
    }

    private fun popBackStack() {
        viewModelScope.launch {
            sendUiEvent(UiEvent.PopBackStack)
        }
    }

//    fun updateDateTime(zonedDateTime: ZonedDateTime) {
//        uiState = uiState.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
//    }
//
//    fun upsertDiary(
//        diary: Diary,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (uiState.selectedDiaryId != null) {
//                updateDiary(diary = diary, onSuccess = onSuccess, onError = onError)
//            } else {
//                insertDiary(diary = diary, onSuccess = onSuccess, onError = onError)
//            }
//        }
//    }
//
//    private suspend fun insertDiary(
//        diary: Diary,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit,
//    ) {
//        val result = MongoDB.insertDiary(diary = diary.apply {
//            if (uiState.updatedDateTime != null) {
//                date = uiState.updatedDateTime!!
//            }
//        })
//        if (result is RequestState.Success) {
//            uploadImagesToFirebase()
//            withContext(Dispatchers.Main) {
//                onSuccess()
//            }
//        } else if (result is RequestState.Error) {
//            withContext(Dispatchers.Main) {
//                onError(result.error.message.toString())
//            }
//        }
//    }
//
//    private suspend fun updateDiary(
//        diary: Diary,
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        val result = MongoDB.updateDiary(diary = diary.apply {
//            _id = ObjectId.invoke(uiState.selectedDiaryId!!)
//            date = if (uiState.updatedDateTime != null) {
//                uiState.updatedDateTime!!
//            } else {
//                uiState.selectedDiary!!.date
//            }
//        })
//        if (result is RequestState.Success) {
//            uploadImagesToFirebase()
//            deleteImagesFromFirebase()
//            withContext(Dispatchers.Main) {
//                onSuccess()
//            }
//        } else if (result is RequestState.Error) {
//            withContext(Dispatchers.Main) {
//                onError(result.error.message.toString())
//            }
//        }
//    }
//
//    fun deleteDiary(
//        onSuccess: () -> Unit,
//        onError: (String) -> Unit
//    ) {
//        viewModelScope.launch(Dispatchers.IO) {
//            if (uiState.selectedDiaryId != null) {
//                val result = MongoDB.deleteDiary(id = ObjectId.invoke(uiState.selectedDiaryId!!))
//                if (result is RequestState.Success) {
//                    withContext(Dispatchers.Main) {
//                        uiState.selectedDiary?.let {
//                            deleteImagesFromFirebase(images = it.images)
//                        }
//                        onSuccess()
//                    }
//                } else if (result is RequestState.Error) {
//                    withContext(Dispatchers.Main) {
//                        onError(result.error.message.toString())
//                    }
//                }
//            }
//        }
//    }
//
//    fun addImage(image: Uri, imageType: String) {
//        val remoteImagePath = "images/${FirebaseAuth.getInstance().currentUser?.uid}/" +
//                "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
//        galleryState.addImage(
//            GalleryImage(
//                image = image,
//                remoteImagePath = remoteImagePath
//            )
//        )
//    }
//
//    private fun uploadImagesToFirebase() {
//        val storage = FirebaseStorage.getInstance().reference
//        galleryState.images.forEach { galleryImage ->
//            val imagePath = storage.child(galleryImage.remoteImagePath)
//            imagePath.putFile(galleryImage.image)
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
//        }
//    }
//
//    private fun deleteImagesFromFirebase(images: List<String>? = null) {
//        val storage = FirebaseStorage.getInstance().reference
//        if (images != null) {
//            images.forEach { remotePath ->
//                storage.child(remotePath).delete()
//                    .addOnFailureListener {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            imageToDeleteDao.addImageToDelete(
//                                ImageToDelete(remoteImagePath = remotePath)
//                            )
//                        }
//                    }
//            }
//        } else {
//            galleryState.imagesToBeDeleted.map { it.remoteImagePath }.forEach { remotePath ->
//                storage.child(remotePath).delete()
//                    .addOnFailureListener {
//                        viewModelScope.launch(Dispatchers.IO) {
//                            imageToDeleteDao.addImageToDelete(
//                                ImageToDelete(remoteImagePath = remotePath)
//                            )
//                        }
//                    }
//            }
//        }
//    }
//
//    private fun extractImagePath(fullImageUrl: String): String {
//        val chunks = fullImageUrl.split("%2F")
//        val imageName = chunks[2].split("?").first()
//        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
//    }
}
