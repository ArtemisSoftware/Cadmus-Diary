package com.artemissoftware.cadmusdiary.presentation.screens.auth

import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.data.repository.AuthenticationRepositoryImpl
import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.usecases.SignInUseCase
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.presentation.components.events.MessageBarType
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import com.artemissoftware.cadmusdiary.util.UiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

class AuthenticationViewModel(
    // private val signInWithMongoAtlasUseCase: SignInWithMongoAtlasUseCase,
) : UiEventViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state: StateFlow<AuthenticationState> = _state.asStateFlow()

    fun onTriggerEvent(event: AuthenticationEvents) {
        when (event) {
            is AuthenticationEvents.SetLoading -> {
                setLoading(loading = event.loading)
            }
            is AuthenticationEvents.SignInWithMongoAtlas -> {
                signInWithMongoAtlas(tokenId = event.tokenId)
            }
        }
    }

    private fun setLoading(loading: Boolean) = with(_state) {
        update {
            it.copy(
                isLoading = loading,
            )
        }
    }

    private fun setAuthenticated(authenticated: Boolean) = with(_state) {
        update {
            it.copy(
                authenticated = authenticated,
            )
        }
    }

    private fun signInWithMongoAtlas(tokenId: String) {
        viewModelScope.launch {
            val repo = AuthenticationRepositoryImpl()
            val result = SignInUseCase(repo).invoke(tokenId) // TODO injectar

            withContext(Dispatchers.Main) {
                when (result) {
                    is RequestState.Success -> {
                        if (result.data) {
                            sendUiEvent(
                                UiEvent.ShowMessageBar(
                                    MessageBarType.Success(
                                        UiText.StringResource(
                                            R.string.successfully_authenticated,
                                        ),
                                    ),
                                ),
                            )
                            delay(600.milliseconds)

                            sendUiEvent(UiEvent.NavigatePopCurrent(Screen.Home.route))
                        } else {
                            sendUiEvent(
                                UiEvent.ShowMessageBar(MessageBarType.Error(Exception("User is not logged in."))),
                            )
                        }
                        setAuthenticated(result.data)
                    }

                    is RequestState.Error -> {
                        UiEvent.ShowMessageBar(MessageBarType.Error(result.error as Exception))
                        setAuthenticated(false)
                    }

                    else -> Unit
                }
            }
            setLoading(false)
        }
    }
}
