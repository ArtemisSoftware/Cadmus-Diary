package com.artemissoftware.cadmusdiary.authentication.presentation.auth

import androidx.lifecycle.viewModelScope
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.artemissoftware.cadmusdiary.authentication.domain.usecase.SignInUseCase
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.core.ui.util.MessageBarType
import com.artemissoftware.cadmusdiary.core.ui.util.uievents.UiEvent
import com.artemissoftware.cadmusdiary.core.ui.util.uievents.UiEventViewModel
import com.artemissoftware.cadmusdiary.core.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
) : UiEventViewModel() {

    private val _state = MutableStateFlow(AuthenticationState())
    val state: StateFlow<AuthenticationState> = _state.asStateFlow()

    fun onTriggerEvent(event: AuthenticationEvents) {
        when (event) {
            is AuthenticationEvents.SetLoading -> {
                setLoading(loading = event.loading)
            }
            is AuthenticationEvents.SignIn -> {
                signIn(tokenId = event.tokenId)
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
                isLoading = false,
            )
        }
    }

    private fun signIn(tokenId: String) {
        viewModelScope.launch {
            val result = signInUseCase(tokenId)

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
    }
}
