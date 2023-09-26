package com.artemissoftware.cadmusdiary.presentation.screens.home

import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.core.ui.connectivity.ConnectivityObserver
import com.artemissoftware.cadmusdiary.core.ui.connectivity.NetworkConnectivityObserver
import com.artemissoftware.cadmusdiary.data.repository.Diaries
import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.usecases.DeleteAllDiariesUseCase
import com.artemissoftware.cadmusdiary.domain.usecases.GetDiaryImagesUseCase
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import com.artemissoftware.cadmusdiary.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val connectivity: NetworkConnectivityObserver,
    private val getDiaryImagesUseCase: GetDiaryImagesUseCase,
    private val deleteAllDiariesUseCase: DeleteAllDiariesUseCase,
) : UiEventViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

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
        }
    }

    fun getDiaries(/*zonedDateTime: ZonedDateTime? = null*/) {
//        dateIsSelected = zonedDateTime != null
//        diaries.value = RequestState.Loading
//        if (dateIsSelected && zonedDateTime != null) {
//            observeFilteredDiaries(zonedDateTime = zonedDateTime)
//        } else {
        observeAllDiaries()
//        }
    }

    private fun observeAllDiaries() {
        /*allDiariesJob = */viewModelScope.launch {
//            if (::filteredDiariesJob.isInitialized) {
//                filteredDiariesJob.cancelAndJoin()
//            }
            MongoDB.getAllDiaries().collect { result ->
                updateDiaries(result)
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

    private fun checkConnectivity() = with(_state) {
        viewModelScope.launch {
            connectivity.observe().collect { status ->
                update {
                    it.copy(network = status)
                }
            }
        }
    }

    private fun openDiaryGallery(diaryId: ObjectId) = with(_state) {
        val images = value.diariesImages.toMutableList()
        val ll = images.find { it.id == diaryId.toString() }
        ll?.let {
            images.removeIf { it.id == diaryId.toString() }
            images.add(
                DiariesImageState(
                    id = diaryId.toString(),
                    uris = it.uris,
                    isOpened = !ll.isOpened,
                ),
            )
        } ?: run {
            images.add(
                DiariesImageState(
                    id = diaryId.toString(),
                    isLoading = true,
                    isOpened = true,
                ),
            )
        }

        update {
            it.copy(diariesImages = images)
        }
    }

    private fun signOut() {
        viewModelScope.launch {
            // TODO: chamar o SignOutUseCase

            withContext(Dispatchers.IO) {
                val user = App.create(APP_ID).currentUser
                if (user != null) {
                    user.logOut()

                    withContext(Dispatchers.Main) {
                        sendUiEvent(UiEvent.NavigatePopCurrent(Screen.Authentication.route))
                    }
                }
            }
        }
    }

    private fun navigate(route: String) {
        viewModelScope.launch {
            sendUiEvent(UiEvent.Navigate(route))
        }
    }

    private fun getImages(diaryId: ObjectId, list: List<String>) = with(_state) {
        viewModelScope.launch {
            val result = getDiaryImagesUseCase.invoke(diaryId.toString(), list)

            when(result) {
                is RequestState.Success -> {
                    // TODO: Simplificar esta lógica
                    val im = value.diariesImages.toMutableList()
                    im.removeIf { it.id == result.data.id }
                    im.add(
                        DiariesImageState(
                            id = diaryId.toString(),
                            isOpened = true,
                            uris = result.data.images.map { it.toUri() },
                        ),
                    )

                    update {
                        it.copy(diariesImages = im)
                    }
                }
                is RequestState.Error -> {
                    val im = value.diariesImages.toMutableList()
                    im.removeIf { it.id == diaryId.toString() }
                    im.add(
                        DiariesImageState(
                            id = diaryId.toString(),
                            isLoading = false,
                        ),
                    )

                    update {
                        it.copy(diariesImages = im)
                    }
                    sendUiEvent(UiEvent.ShowToast(UiText.DynamicString("Images not uploaded yet." + "Wait a little bit, or try uploading again."), Toast.LENGTH_SHORT))
                }
                else -> Unit // TODO: caso de erro
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

    // ----------------------

//
//    private lateinit var allDiariesJob: Job
//    private lateinit var filteredDiariesJob: Job
//

//    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)
//    var dateIsSelected by mutableStateOf(false)
//        private set
//
//    init {
//        getDiaries()
//        viewModelScope.launch {
//            connectivity.observe().collect { network = it }
//        }
//    }

//
//    private fun observeFilteredDiaries(zonedDateTime: ZonedDateTime) {
//        filteredDiariesJob = viewModelScope.launch {
//            if (::allDiariesJob.isInitialized) {
//                allDiariesJob.cancelAndJoin()
//            }
//            MongoDB.getFilteredDiaries(zonedDateTime = zonedDateTime).collect { result ->
//                diaries.value = result
//            }
//        }
//    }
//
//    fun deleteAllDiaries(
//        onSuccess: () -> Unit,
//        onError: (Throwable) -> Unit
//    ) {
//        if (network == ConnectivityObserver.Status.Available) {
//            val userId = FirebaseAuth.getInstance().currentUser?.uid
//            val imagesDirectory = "images/${userId}"
//            val storage = FirebaseStorage.getInstance().reference
//            storage.child(imagesDirectory)
//                .listAll()
//                .addOnSuccessListener {
//                    it.items.forEach { ref ->
//                        val imagePath = "images/${userId}/${ref.name}"
//                        storage.child(imagePath).delete()
//                            .addOnFailureListener {
//                                viewModelScope.launch(Dispatchers.IO) {
//                                    imageToDeleteDao.addImageToDelete(
//                                        ImageToDelete(
//                                            remoteImagePath = imagePath
//                                        )
//                                    )
//                                }
//                            }
//                    }
//                    viewModelScope.launch(Dispatchers.IO) {
//                        val result = MongoDB.deleteAllDiaries()
//                        if (result is RequestState.Success) {
//                            withContext(Dispatchers.Main) {
//                                onSuccess()
//                            }
//                        } else if (result is RequestState.Error) {
//                            withContext(Dispatchers.Main) {
//                                onError(result.error)
//                            }
//                        }
//                    }
//                }
//                .addOnFailureListener { onError(it) }
//        } else {
//            onError(Exception("No Internet Connection."))
//        }
//    }
}
