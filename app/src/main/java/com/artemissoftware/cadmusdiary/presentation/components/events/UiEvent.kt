package com.artemissoftware.cadmusdiary.presentation.components.events

sealed class UiEvent {

    data class ShowMessageBar(val messageBarType: MessageBarType) : UiEvent()

    object PopBackStack : UiEvent()

    data class Navigate(val route: String) : UiEvent()
}
