package com.artemissoftware.cadmusdiary.write.presentation.write

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.core.domain.RequestState
import com.core.domain.usecases.GetDiaryImagesUseCase
import com.core.ui.models.GalleryImage
import com.core.ui.models.MoodUI
import com.core.ui.models.toMoodUi
import com.core.ui.util.UiText
import com.core.ui.util.uievents.UiEvent
import com.core.ui.util.uievents.UiEventViewModel
import com.core.domain.models.Diary
import com.artemissoftware.cadmusdiary.navigation.Screen.Companion.WRITE_SCREEN_ARGUMENT_KEY
import com.artemissoftware.cadmusdiary.util.extensions.toRealmInstant
import com.artemissoftware.cadmusdiary.write.domain.usecases.DeleteDiaryUseCase
import com.artemissoftware.cadmusdiary.write.domain.usecases.DeleteImagesUseCase
import com.artemissoftware.cadmusdiary.write.domain.usecases.GetDiaryUseCase
import com.artemissoftware.cadmusdiary.write.domain.usecases.InsertDiaryUseCase
import com.artemissoftware.cadmusdiary.write.domain.usecases.UpdateDiaryUseCase
import com.artemissoftware.cadmusdiary.write.domain.usecases.UploadImagesUseCase
import com.artemissoftware.cadmusdiary.write.presentation.write.mappers.toPictures
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val getDiaryUseCase: GetDiaryUseCase,
    private val getDiaryImagesUseCase: GetDiaryImagesUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val insertDiaryUseCase: InsertDiaryUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
    private val uploadImagesUseCase: UploadImagesUseCase,
    private val deleteImagesUseCase: DeleteImagesUseCase,
) : UiEventViewModel() {

    private val _state = MutableStateFlow(WriteState())
    val state: StateFlow<WriteState> = _state.asStateFlow()

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

            is WriteEvents.AddImage -> {
                addImage(image = event.image, imageType = getImagePath(event.image))
            }

            is WriteEvents.ZoomInImage -> {
                setSelectedImage(event.image)
            }

            WriteEvents.ZoomOutImage -> {
                setSelectedImage()
            }

            is WriteEvents.DeleteImage -> {
                deleteImage(event.image)
            }
        }
    }

    private fun setSelectedDiary(diary: Diary) = with(_state) {
        update {
            it.copy(
                selectedDiary = diary,
                title = diary.title,
                description = diary.description,
                mood = diary.mood.toMoodUi(),
            )
        }
    }

    private fun updateGallery(images: List<String>) = with(_state) {
        val resultGallery = images.map {
            GalleryImage(
                image = it.toUri(),
                remoteImagePath = extractImagePath(
                    fullImageUrl = it,
                ),
            )
        }

        update {
            it.copy(
                galleryState = it.galleryState.copy(
                    images = resultGallery,
                ),
            )
        }
    }

    private fun setSelectedImage(image: GalleryImage? = null) = with(_state) {
        update {
            it.copy(selectedImage = image)
        }
    }

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

    private fun setMood(mood: MoodUI) = with(_state) {
        update {
            it.copy(mood = mood)
        }
    }

    private fun updateDateTime(zonedDateTime: ZonedDateTime) = with(_state) {
        update {
            it.copy(updatedDateTime = zonedDateTime.toInstant().toRealmInstant())
        }
    }

    private fun addImage(image: Uri, imageType: String) = with(_state) {
        val imageList = value.galleryState.images.toMutableList()
        imageList.add(
            GalleryImage(
                image = image,
                remoteImagePath = getRemoteImagePath(image, imageType),
            ),
        )

        update {
            it.copy(
                galleryState = it.galleryState.copy(
                    images = imageList,
                ),
            )
        }
    }

    private fun createDiary(): Diary = with(_state) {
        val diary = Diary().apply {
            this.title = value.title
            this.description = value.description
            this.mood = value.mood.name

            if (value.updatedDateTime != null) {
                this.date = value.updatedDateTime!!
            } else {
                value.selectedDiary?.let { this.date = it.date }
            }
            this.images = value.galleryState.images.map { it.remoteImagePath }.toRealmList()
        }

        value.selectedDiaryId?.let {
            diary.apply {
                _id = ObjectId.invoke(it)
            }
        }

        return diary
    }

    private fun popBackStack() {
        viewModelScope.launch {
            sendUiEvent(UiEvent.PopBackStack)
        }
    }

    private fun fetchSelectedDiary() = with(_state.value) {
        selectedDiaryId?.let { diaryId ->

            viewModelScope.launch {
                getDiaryUseCase(diaryId = diaryId).collect { request ->

                    when (request) {
                        is com.core.domain.RequestState.Success -> {
                            val diary = request.data
                            setSelectedDiary(diary)
                            fetchImages(diary._id.toString(), diary.images)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun fetchImages(diaryId: String, images: List<String>) = with(_state) {
        viewModelScope.launch {
            getDiaryImagesUseCase.invoke(diaryId, images).collect { result ->
                when (result) {
                    is com.core.domain.RequestState.Success -> {
                        updateGallery(result.data.images)
                    }
                    is com.core.domain.RequestState.Error -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.DynamicString("Images not uploaded yet." + "Wait a little bit, or try uploading again."), Toast.LENGTH_SHORT))
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun save() = with(_state.value) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val diary = createDiary()

            if (selectedDiaryId != null) {
                updateDiary(diary = diary)
            }
            else {
                insertDiary(diary = diary)
            }
        } else {
            viewModelScope.launch {
                sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT))
            }
        }
    }

    private fun insertDiary(diary: Diary) {
        viewModelScope.launch {
            val result = insertDiaryUseCase(diary = diary)

            when(result) {
                is com.core.domain.RequestState.Success -> {
                    uploadImages()
                    sendUiEvent(UiEvent.PopBackStack)
                }
                is com.core.domain.RequestState.Error -> {
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                }
                else -> Unit
            }
        }
    }

    private fun updateDiary(diary: Diary) {
        viewModelScope.launch {
            val result = updateDiaryUseCase(diary = diary)
            when(result) {
                is com.core.domain.RequestState.Success -> {
                    updateImages()
                    sendUiEvent(UiEvent.PopBackStack)
                }
                is com.core.domain.RequestState.Error -> {
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                }
                else -> Unit
            }
        }
    }

    private fun updateImages(images: List<String>? = null) = with(_state.value) {
        val imagesToBeDeleted = images ?: galleryState.imagesToBeDeleted.map { it.remoteImagePath }

        viewModelScope.launch {
            uploadImagesUseCase.invoke(galleryState.images.toPictures())
            deleteImagesUseCase.invoke(imagesToBeDeleted)
        }
    }

    private fun deleteDiary() = with(_state.value) {
        viewModelScope.launch {
            selectedDiaryId?.let { diaryId ->
                val result = deleteDiaryUseCase.invoke(diaryId, images = selectedDiary?.images)

                when (result) {
                    is com.core.domain.RequestState.Success -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.deleted), Toast.LENGTH_SHORT))
                        sendUiEvent(UiEvent.PopBackStack)
                    }
                    is com.core.domain.RequestState.Error -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun uploadImages() = with(_state.value) {
        viewModelScope.launch {
            uploadImagesUseCase.invoke(galleryState.images.toPictures())
        }
    }

    private fun deleteImage(image: GalleryImage) = with(_state) {
        val imageList = value.galleryState.images.toMutableList()
        imageList.removeIf { it.image == image.image }

        val imagesToBeDeleted = value.galleryState.imagesToBeDeleted.toMutableList()
        imagesToBeDeleted.add(image)

        update {
            it.copy(
                galleryState = it.galleryState.copy(
                    images = imageList,
                    imagesToBeDeleted = imagesToBeDeleted,
                ),
            )
        }
    }

//    private fun deleteImages(images: List<String>? = null) = with(_state.value) {
//        val imagesToBeDeleted = images ?: galleryState.imagesToBeDeleted.map { it.remoteImagePath }
//
//        viewModelScope.launch {
//            uploadImagesUseCase.invoke(galleryState.images)
//        }
//    }

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

    private fun extractImagePath(fullImageUrl: String): String {
        val chunks = fullImageUrl.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${FirebaseAuth.getInstance().currentUser?.uid}/$imageName"
    }

    private fun getRemoteImagePath(image: Uri, imageType: String): String {
        return "images/${FirebaseAuth.getInstance().currentUser?.uid}/" +
            "${image.lastPathSegment}-${System.currentTimeMillis()}.$imageType"
    }

    private fun getImagePath(image: Uri): String {
        return application.contentResolver.getType(image)?.split("/")?.last() ?: "jpg"
    }
}
