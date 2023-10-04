package com.artemissoftware.cadmusdiary

import androidx.lifecycle.ViewModel
import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val mongoRepository: MongoRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state.asStateFlow()

    init {
        mongoRepository.configureTheRealm()
    }

    fun onTriggerEvent(event: MainActivityEvents) {
        when (event) {
            MainActivityEvents.FinishSplash -> {
                finishSplash()
            }
        }
    }

    private fun finishSplash() = with(_state) {
        update {
            it.copy(keepSplashOpened = false)
        }
    }
}
