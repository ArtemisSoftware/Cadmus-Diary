package com.artemissoftware.cadmusdiary.presentation.screens.home

import androidx.lifecycle.viewModelScope
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
        }
    }

    private fun updateSignOutDialog(open: Boolean) = with(_state) {
        update {
            it.copy(signOutDialogOpened = open)
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
}
