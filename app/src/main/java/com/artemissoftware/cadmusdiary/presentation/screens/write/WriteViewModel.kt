package com.artemissoftware.cadmusdiary.presentation.screens.write

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.model.Diary
import com.artemissoftware.cadmusdiary.domain.model.Mood
import com.artemissoftware.cadmusdiary.navigation.Screen.Companion.WRITE_SCREEN_ARGUMENT_KEY
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import com.artemissoftware.cadmusdiary.util.UiText
import com.artemissoftware.cadmusdiary.util.extensions.toRealmInstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime

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

            is WriteEvents.SetMood -> {
                setMood(mood = event.mood)
            }

            WriteEvents.SaveDiary -> {
                save()
            }

            is WriteEvents.UpdateDateTime -> {
                updateDateTime(event.zonedDateTime)
            }

            WriteEvents.DeleteDiary -> {
                deleteDiary()
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

    private fun updateDateTime(zonedDateTime: ZonedDateTime) = with(_state) {
        update {
            it.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
        }
    }

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

    private fun save() = with(_state.value) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val diary = Diary().apply {
                this.title = _state.value.title
                this.description = _state.value.description
                this.mood = _state.value.mood.name

                if (_state.value.updatedDateTime != null) {
                    this.date = _state.value.updatedDateTime!!
                } else {
                    _state.value.selectedDiary?.let { this.date = it.date }
                }
                // this.images = galleryState.images.map { it.remoteImagePath }.toRealmList()
            }

            selectedDiaryId?.let {
                diary.apply {
                    _id = ObjectId.invoke(it)
                }

                updateDiary(diary = diary)
            } ?: run {
                insertDiary(diary = diary)
            }
        } else {
            viewModelScope.launch {
                sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT))
            }
        }
    }

    private fun insertDiary(
        diary: Diary,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO : InsertDiaryUseCase

            val result = MongoDB.insertDiary(diary = diary)
            if (result is RequestState.Success) {
//            uploadImagesToFirebase()
                withContext(Dispatchers.Main) {
                    sendUiEvent(UiEvent.PopBackStack)
                }
            } else if (result is RequestState.Error) {
                withContext(Dispatchers.Main) {
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                }
            }
        }
    }

    private fun updateDiary(diary: Diary) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = MongoDB.updateDiary(diary = diary)
            if (result is RequestState.Success) {
//                uploadImagesToFirebase()
//                deleteImagesFromFirebase()
                withContext(Dispatchers.Main) {
                    sendUiEvent(UiEvent.PopBackStack)
                }
            } else if (result is RequestState.Error) {
                withContext(Dispatchers.Main) {
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                }
            }
        }
    }

    private fun deleteDiary() = with(_state.value) {
        viewModelScope.launch(Dispatchers.IO) {
            selectedDiaryId?.let {
                // TODO: DeleteDiaryUseCase

                val result = MongoDB.deleteDiary(id = ObjectId.invoke(it))

                when (result) {
                    is RequestState.Success -> {
                        withContext(Dispatchers.Main) {
//                        uiState.selectedDiary?.let {
//                            deleteImagesFromFirebase(images = it.images)
//                        }
                            sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.deleted), Toast.LENGTH_SHORT))
                            sendUiEvent(UiEvent.PopBackStack)
                        }
                    }
                    is RequestState.Error -> {
                        withContext(Dispatchers.Main) {
                            sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

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
