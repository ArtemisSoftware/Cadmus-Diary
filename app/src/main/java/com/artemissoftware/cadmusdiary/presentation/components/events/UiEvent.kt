package com.artemissoftware.cadmusdiary.presentation.components.events

import com.artemissoftware.cadmusdiary.util.UiText

sealed class UiEvent {

    data class ShowMessageBar(val messageBarType: MessageBarType) : UiEvent()

    object PopBackStack : UiEvent()

    object CloseNavigationDrawer : UiEvent()

    data class Navigate(val route: String) : UiEvent()

    data class NavigatePopCurrent(val route: String) : UiEvent()

    data class ShowToast(val uiText: UiText, val duration: Int) : UiEvent()
}
