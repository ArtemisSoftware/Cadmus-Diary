package com.artemissoftware.cadmusdiary.presentation.components.events

sealed class UiEvent {

    data class ShowMessageBar(val messageBarType: MessageBarType) : UiEvent()

    object PopBackStack : UiEvent()

    data class Navigate(val route: String) : UiEvent()

    data class NavigatePopCurrent(val route: String) : UiEvent()
}
