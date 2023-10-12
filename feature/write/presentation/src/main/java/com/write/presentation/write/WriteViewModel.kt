package com.write.presentation.write

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.artemissoftware.navigation.Screen
import com.core.domain.RequestState
import com.core.domain.models.JournalEntry
import com.core.domain.usecases.GetDiaryImagesUseCase
import com.core.ui.models.GalleryImage
import com.core.ui.models.MoodUI
import com.core.ui.models.toMoodUi
import com.core.ui.util.UiText
import com.core.ui.util.uievents.UiEvent
import com.core.ui.util.uievents.UiEventViewModel
import com.write.domain.usecases.DeleteDiaryUseCase
import com.write.domain.usecases.DeleteImagesUseCase
import com.write.domain.usecases.GetDiaryUseCase
import com.write.domain.usecases.GetRemoteImagePathFromUrlUseCase
import com.write.domain.usecases.GetRemoteImagePathUseCase
import com.write.domain.usecases.InsertDiaryUseCase
import com.write.domain.usecases.UpdateDiaryUseCase
import com.write.domain.usecases.UploadImagesUseCase
import com.write.presentation.R
import com.write.presentation.write.mappers.toPictures
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.Instant
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
    private val getRemoteImagePathUseCase: GetRemoteImagePathUseCase,
    private val getRemoteImagePathFromUrlUseCase: GetRemoteImagePathFromUrlUseCase,
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
                    key = Screen.WRITE_SCREEN_ARGUMENT_KEY,
                ),
            )
        }
    }

    private fun fetchSelectedDiary() = with(_state.value) {
        selectedDiaryId?.let { diaryId ->

            viewModelScope.launch {
                getDiaryUseCase(diaryId = diaryId).collect { request ->

                    when (request) {
                        is RequestState.Success -> {
                            val diary = request.data
                            setSelectedDiary(diary)
                            fetchImages(diary.id, diary.images)
                        }
                        else -> Unit
                    }
                }
            }
        }
    }

    private fun setSelectedDiary(journalEntry: JournalEntry) = with(_state) {
        update {
            it.copy(
                selectedDiary = journalEntry,
                title = journalEntry.title,
                description = journalEntry.description,
                mood = journalEntry.mood.toMoodUi(),
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

    private fun updateGallery(images: List<String>) = with(_state) {
        val resultGallery = images.map {
            GalleryImage(
                image = it.toUri(),
                remoteImagePath = getRemoteImagePathFromUrlUseCase(
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
            it.copy(updatedDateTime = zonedDateTime.toInstant())
        }
    }

    private fun addImage(image: Uri, imageType: String) = with(_state) {
        val imageList = value.galleryState.images.toMutableList()
        imageList.add(
            GalleryImage(
                image = image,
                remoteImagePath = getRemoteImagePathUseCase(image.toString(), imageType),
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

    private fun popBackStack() {
        viewModelScope.launch {
            sendUiEvent(UiEvent.PopBackStack)
        }
    }

    private fun fetchImages(diaryId: String, images: List<String>) = with(_state) {
        viewModelScope.launch {
            getDiaryImagesUseCase.invoke(diaryId, images).collect { result ->
                when (result) {
                    is RequestState.Success -> {
                        updateGallery(result.data.images)
                    }
                    is RequestState.Error -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.DynamicString("Images not uploaded yet." + "Wait a little bit, or try uploading again."), Toast.LENGTH_SHORT))
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun save() = with(_state.value) {
        if (title.isNotEmpty() && description.isNotEmpty()) {
            val journalEntry = createJournalEntry()

            if (selectedDiaryId != null) {
                updateDiary(journalEntry = journalEntry)
            }
            else {
                insertDiary(journalEntry = journalEntry)
            }
        } else {
            viewModelScope.launch {
                sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.fields_cannot_be_empty), Toast.LENGTH_SHORT))
            }
        }
    }

    private fun createJournalEntry(): JournalEntry = with(_state) {
        var id = ObjectId.invoke()
        value.selectedDiaryId?.let { id = ObjectId.invoke(it) }

        val date = when {
            (value.updatedDateTime != null) -> {
                value.updatedDateTime!!
            }
            (value.selectedDiary != null) -> {
                value.selectedDiary!!.date
            }

            else -> {
                Instant.now()
            }
        }

        return JournalEntry(
            id = id.toHexString(),
            title = value.title,
            description = value.description,
            mood = value.mood.name,
            images = value.galleryState.images.map { it.remoteImagePath },
            date = date,
        )
    }

    private fun insertDiary(journalEntry: JournalEntry) {
        viewModelScope.launch {
            val result = insertDiaryUseCase(journalEntry = journalEntry)

            when(result) {
                is RequestState.Success -> {
                    uploadImages()
                    sendUiEvent(UiEvent.PopBackStack)
                }
                is RequestState.Error -> {
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                }
                else -> Unit
            }
        }
    }

    private fun updateDiary(journalEntry: JournalEntry) {
        viewModelScope.launch {
            val result = updateDiaryUseCase(journalEntry = journalEntry)
            when(result) {
                is RequestState.Success -> {
                    updateImages()
                    sendUiEvent(UiEvent.PopBackStack)
                }
                is RequestState.Error -> {
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
                    is RequestState.Success -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.deleted), Toast.LENGTH_SHORT))
                        sendUiEvent(UiEvent.PopBackStack)
                    }
                    is RequestState.Error -> {
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

    private fun getImagePath(image: Uri): String {
        return application.contentResolver.getType(image)?.split("/")?.last() ?: "jpg"
    }
}
