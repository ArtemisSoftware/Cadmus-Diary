package com.artemissoftware.cadmusdiary.home.presentation.home

import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.core.data.repository.Diaries
import com.artemissoftware.cadmusdiary.core.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.core.domain.usecases.GetDiaryImagesUseCase
import com.artemissoftware.cadmusdiary.core.ui.connectivity.ConnectivityObserver
import com.artemissoftware.cadmusdiary.core.ui.connectivity.NetworkConnectivityObserver
import com.artemissoftware.cadmusdiary.core.ui.util.UiText
import com.artemissoftware.cadmusdiary.core.ui.util.uievents.UiEvent
import com.artemissoftware.cadmusdiary.core.ui.util.uievents.UiEventViewModel
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.artemissoftware.cadmusdiary.home.domain.usecases.SignOutUseCase
import com.artemissoftware.cadmusdiary.home.domain.usecases.DeleteAllDiariesUseCase
import com.artemissoftware.cadmusdiary.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val getDiaryImagesUseCase: GetDiaryImagesUseCase,
    private val deleteAllDiariesUseCase: DeleteAllDiariesUseCase,
    private val signOutUseCase: SignOutUseCase,
) : UiEventViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var allDiariesJob: Job? = null
    private var filteredDiariesJob: Job? = null

    init {
        getDiaries()
        checkConnectivity()
    }

    fun onTriggerEvent(event: HomeEvents) {
        when (event) {
            HomeEvents.CloseSignOutDialog -> {
                updateSignOutDialog(open = false)
            }
            HomeEvents.SignOutGoogleAccount -> {
                signOut()
            }

            HomeEvents.OpenSignOutDialog -> {
                updateSignOutDialog(open = true)
            }

            is HomeEvents.Navigate -> {
                navigate(event.route)
            }

            is HomeEvents.FetchImages -> {
                getImages(diaryId = event.diaryId, list = event.list)
            }

            is HomeEvents.OpenDiaryGallery -> {
                openDiaryGallery(event.diaryId)
            }

            HomeEvents.CloseDeleteAllDialog -> {
                updateDeleteAllDialog(open = false)
            }
            HomeEvents.OpenDeleteAllDialog -> {
                updateDeleteAllDialog(open = true)
            }

            HomeEvents.DeleteAllDiaries -> {
                deleteAllDiaries()
            }

            is HomeEvents.GetDiaries -> {
                getDiaries(event.zonedDateTime)
            }
        }
    }

    private fun updateSignOutDialog(open: Boolean) = with(_state) {
        update {
            it.copy(signOutDialogOpened = open)
        }
    }

    private fun updateDeleteAllDialog(open: Boolean) = with(_state) {
        update {
            it.copy(deleteAllDialogOpened = open)
        }
    }

    private fun updateDiaries(diaries: Diaries) = with(_state) {
        update {
            it.copy(diaries = diaries)
        }
    }

    private fun updateDiariesImages(diaryId: String, urls: List<String> = emptyList()) = with(_state) {
        val diariesImages = value.diariesImages.toMutableList()
        diariesImages.removeIf { it.id == diaryId }
        diariesImages.add(
            DiariesImageState(
                id = diaryId,
                isOpened = urls.isNotEmpty(),
                uris = urls.map { it.toUri() },
            ),
        )

        update {
            it.copy(diariesImages = diariesImages)
        }
    }

    private fun updateDiariesImages(diaryId: String, diariesImages: DiariesImageState?) = with(_state) {
        val images = value.diariesImages.toMutableList()

        DiariesImageState(
            id = diaryId,
            uris = diariesImages?.uris ?: emptyList(),
            isLoading = diariesImages?.uris?.isEmpty() ?: true,
            isOpened = diariesImages?.isOpened ?: true,
        )

        diariesImages?.let { item ->
            images.removeIf { it.id == diaryId }
        }

        update {
            it.copy(diariesImages = images)
        }
    }

    private fun navigate(route: String) {
        viewModelScope.launch {
            sendUiEvent(UiEvent.Navigate(route))
        }
    }

    private fun getDiaries(zonedDateTime: ZonedDateTime? = null) = with(_state) {
        update {
            it.copy(
                dateIsSelected = zonedDateTime != null,
                diaries = RequestState.Loading,
            )
        }

        if (value.dateIsSelected && zonedDateTime != null) {
            observeFilteredDiaries(zonedDateTime = zonedDateTime)
        } else {
            observeAllDiaries()
        }
    }

    private fun observeAllDiaries() {
        allDiariesJob = viewModelScope.launch {
            filteredDiariesJob?.cancelAndJoin()
            MongoDB.getAllDiaries().collect { result ->
                updateDiaries(result)
            }
        }
    }

    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) = with(_state) {
        filteredDiariesJob = viewModelScope.launch {
            allDiariesJob?.cancelAndJoin()
            MongoDB.getFilteredDiaries(zonedDateTime = zonedDateTime).collect { result ->
                update {
                    it.copy(
                        diaries = result,
                    )
                }
            }
        }
    }

    private fun checkConnectivity() = with(_state) {
        viewModelScope.launch {
            connectivity.observe().collect { status ->
                update {
                    it.copy(network = status)
                }
            }
        }
    }

    private fun openDiaryGallery(diaryId: ObjectId) = with(_state.value) {
        val diariesImages = this.diariesImages.toMutableList()
        val diaryImages = diariesImages.find { it.id == diaryId.toString() }
        updateDiariesImages(diaryId = diaryId.toString(), diariesImages = diaryImages)
    }

    private fun signOut() {
        viewModelScope.launch {
            val isLoggedOut = signOutUseCase.invoke()

            if(isLoggedOut) {
                sendUiEvent(UiEvent.NavigatePopCurrent(Screen.Authentication.route))
            }
        }
    }

    private fun getImages(diaryId: ObjectId, list: List<String>) {
        viewModelScope.launch {
            getDiaryImagesUseCase.invoke(diaryId.toString(), list).collect { result ->
                when(result) {
                    is RequestState.Success -> {
                        updateDiariesImages(diaryId = result.data.id, urls = result.data.images)
                    }
                    is RequestState.Error -> {
                        updateDiariesImages(diaryId = diaryId.toString())
                        sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.images_not_uploaded_yet_wait_a_little_bit_or_try_uploading_again), Toast.LENGTH_SHORT))
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun deleteAllDiaries() {
        viewModelScope.launch {
            if (_state.value.network == ConnectivityObserver.Status.Available) {
                val result = deleteAllDiariesUseCase()

                when(result) {
                    is RequestState.Success -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.all_diaries_deleted), Toast.LENGTH_SHORT))
                    }
                    is RequestState.Error -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.DynamicString(result.error.message.toString()), Toast.LENGTH_SHORT))
                    }
                    else -> Unit
                }
            }
            else {
                sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.internet_connection_necessary_for_this_operation), Toast.LENGTH_SHORT))
            }

            sendUiEvent(UiEvent.CloseNavigationDrawer)
        }
    }
}