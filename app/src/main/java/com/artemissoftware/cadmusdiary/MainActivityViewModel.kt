package com.artemissoftware.cadmusdiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artemissoftware.navigation.Screen
import com.core.domain.repository.MongoRepository
import com.core.domain.usecases.CheckUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mongoRepository: MongoRepository,
    private val checkUserLoggedInUseCase: CheckUserLoggedInUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state.asStateFlow()

    init {
        mongoRepository.configureTheRealm()
        checkUserLoggedIn()
    }

    fun onTriggerEvent(event: MainActivityEvents) {
        when (event) {
            MainActivityEvents.FinishSplash -> {
                finishSplash()
            }
        }
    }

    private fun checkUserLoggedIn() = with(_state) {
        viewModelScope.launch {
            val isUserLoggedIn = checkUserLoggedInUseCase()

            val startDestination = if (isUserLoggedIn) {
                Screen.Home.route
            } else {
                Screen.Authentication.route
            }

            update {
                it.copy(userLoggedIn = isUserLoggedIn, startDestination = startDestination)
            }
        }
    }

    private fun finishSplash() = with(_state) {
        update {
            it.copy(keepSplashOpened = false)
        }
    }
}
