package com.artemissoftware.cadmusdiary.presentation.components.events

sealed class UiEvent {

    data class ShowMessageBar(val messageBarType: MessageBarType) : UiEvent()
}
