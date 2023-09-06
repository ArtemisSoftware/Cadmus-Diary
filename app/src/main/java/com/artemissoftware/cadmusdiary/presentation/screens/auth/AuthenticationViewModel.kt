package com.artemissoftware.cadmusdiary.presentation.screens.auth

import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.presentation.components.events.MessageBarType
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEventViewModel
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import com.artemissoftware.cadmusdiary.util.UiText
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
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
            try {
                // TODO : val result = signInWithMongoAtlasUseCase(tokenId = tokenId)

                val isLoggedIn = withContext(Dispatchers.IO) {
                    App.create(APP_ID).login(
                        Credentials.jwt(tokenId),
                    ).loggedIn
                }

                withContext(Dispatchers.Main) {
                    if (isLoggedIn) {
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
                    setAuthenticated(isLoggedIn)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    UiEvent.ShowMessageBar(MessageBarType.Error(e))
                    setAuthenticated(false)
                }
            }
            setLoading(false)
        }
    }
}
