package com.artemissoftware.cadmusdiary.home.presentation.home

import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.core.data.repository.Diaries
import com.core.domain.RequestState
import com.core.domain.usecases.GetDiaryImagesUseCase
import com.core.ui.connectivity.ConnectivityObserver
import com.core.ui.connectivity.NetworkConnectivityObserver
import com.core.ui.util.UiText
import com.core.ui.util.uievents.UiEvent
import com.core.ui.util.uievents.UiEventViewModel
import com.artemissoftware.cadmusdiary.home.domain.usecases.DeleteAllDiariesUseCase
import com.artemissoftware.cadmusdiary.home.domain.usecases.GetAllDiariesUseCase
import com.artemissoftware.cadmusdiary.home.domain.usecases.GetFilteredDiariesUseCase
import com.artemissoftware.cadmusdiary.home.domain.usecases.SignOutUseCase
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.home.presentation.home.DiariesImageState
import com.home.presentation.home.HomeEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.ext.copyFromRealm
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val getDiaryImagesUseCase: GetDiaryImagesUseCase,
    private val deleteAllDiariesUseCase: DeleteAllDiariesUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getAllDiariesUseCase: GetAllDiariesUseCase,
    private val getFilteredDiariesUseCase: GetFilteredDiariesUseCase,
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

            is HomeEvents.CloseDiaryGallery -> {
                closeDiaryGallery(event.diaryId)
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
        updateImagesOnOpenedGalleries(diaries)
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

        diariesImages?.let { _ ->
            images.removeIf { it.id == diaryId }
        }

        images.add(
            DiariesImageState(
                id = diaryId,
                uris = diariesImages?.uris ?: emptyList(),
                isLoading = diariesImages?.uris?.isEmpty() ?: true,
                isOpened = diariesImages?.isOpened ?: true,
            ),
        )

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
                diaries = com.core.domain.RequestState.Loading,
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
            getAllDiariesUseCase().collect { result ->
                updateDiaries(result)
            }
        }
    }

    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) = with(_state) {
        filteredDiariesJob = viewModelScope.launch {
            allDiariesJob?.cancelAndJoin()
            getFilteredDiariesUseCase(zonedDateTime = zonedDateTime).collect { result ->
                updateDiaries(result)
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

    private fun openDiaryGallery(diaryId: String) = with(_state) {
        val allDiaries = (_state.value.diaries as com.core.domain.RequestState.Success).data.values.flatten().map { it.copyFromRealm() }
        val currentDiary = allDiaries.find { it._id.toString() == diaryId }
        val currentNumberOfImages = allDiaries.find { it._id.toString() == diaryId }?.images?.size ?: 0
        val numberOfImages = value.diariesImages.find { it.id == diaryId }?.uris?.size ?: 0

        if(value.diariesImages.none { it.id == diaryId }) {
            updateDiariesImages(diaryId, null)
            return@with
        }
        else if(currentNumberOfImages != numberOfImages) {
            getImages(currentDiary?._id.toString(), currentDiary?.images ?: emptyList())
            return@with
        }

        val images = value.diariesImages.toMutableList()
        images.find { it.id == diaryId }?.let { diariesImages ->
            val image = diariesImages.copy(isOpened = !diariesImages.isOpened)
            images.removeIf { it.id == diaryId }
            images.add(image)
            update {
                it.copy(diariesImages = images)
            }
        }
    }

    private fun closeDiaryGallery(diaryId: String) = with(_state) {
        val images = value.diariesImages.toMutableList()
        images.find { it.id.contains(diaryId) }?.let { diariesImages ->
            val image = diariesImages.copy(isOpened = false)
            images.removeIf { it.id.contains(diaryId) }
            images.add(image)
            update {
                it.copy(diariesImages = images)
            }
        }
    }

    private fun clearGallery(diaryId: String) = with(_state) {
        val images = value.diariesImages.toMutableList()
        images.removeIf { it.id.contains(diaryId) }
        update {
            it.copy(diariesImages = images)
        }
    }

    private fun updateImagesOnOpenedGalleries(diaries: Diaries) {
        val openedGalleries = _state.value.diariesImages.filter { it.isOpened }.map { it.id }
        if(openedGalleries.isNotEmpty()) {
            val allDiaries = (diaries as com.core.domain.RequestState.Success).data.values.flatten().map { it.copyFromRealm() }

            allDiaries.filter { openedGalleries.contains(it._id.toString()) }.forEach {
                updateDiariesImages(it._id.toString(), null)
                if(it.images.isNotEmpty()) {
                    getImages(diaryId = it._id.toString(), list = it.images)
                }
                else {
                    clearGallery(it._id.toString())
                    closeDiaryGallery(it._id.toString())
                }
            }
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            val isLoggedOut = signOutUseCase.invoke()

            if(isLoggedOut) {
                sendUiEvent(UiEvent.NavigatePopCurrent(Screen.Authentication.route))
            }
        }
    }

    private fun getImages(diaryId: String, list: List<String>) {
        viewModelScope.launch {
            getDiaryImagesUseCase.invoke(diaryId, list).collect { result ->
                when(result) {
                    is com.core.domain.RequestState.Success -> {
                        updateDiariesImages(diaryId = result.data.id, urls = result.data.images)
                    }
                    is com.core.domain.RequestState.Error -> {
                        updateDiariesImages(diaryId = diaryId)
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
                    is com.core.domain.RequestState.Success -> {
                        sendUiEvent(UiEvent.ShowToast(UiText.StringResource(R.string.all_diaries_deleted), Toast.LENGTH_SHORT))
                    }
                    is com.core.domain.RequestState.Error -> {
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
