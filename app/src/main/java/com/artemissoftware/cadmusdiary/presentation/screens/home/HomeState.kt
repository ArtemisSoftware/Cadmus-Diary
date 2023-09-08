package com.artemissoftware.cadmusdiary.presentation.screens.home

import com.artemissoftware.cadmusdiary.data.repository.Diaries
import com.artemissoftware.cadmusdiary.domain.RequestState

data class HomeState(
    val signOutDialogOpened: Boolean = false,
    val diaries: Diaries = RequestState.Idle
)
