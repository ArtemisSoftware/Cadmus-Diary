package com.artemissoftware.cadmusdiary.presentation.screens.home

import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.data.repository.Diaries
import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel() : UiEventViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getDiaries()
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

    private fun updateDiaries(diaries: Diaries) = with(_state) {
        update {
            it.copy(diaries = diaries)
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
                        // --navigateToAuth()
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
