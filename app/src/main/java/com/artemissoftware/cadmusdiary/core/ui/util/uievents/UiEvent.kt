package com.artemissoftware.cadmusdiary.core.ui.util.uievents

import com.artemissoftware.cadmusdiary.core.ui.util.MessageBarType
import com.artemissoftware.cadmusdiary.core.ui.util.UiText

sealed class UiEvent {

    data class ShowMessageBar(val messageBarType: MessageBarType) : UiEvent()

    object PopBackStack : UiEvent()

    object CloseNavigationDrawer : UiEvent()

    data class Navigate(val route: String) : UiEvent()

    data class NavigatePopCurrent(val route: String) : UiEvent()

    data class ShowToast(val uiText: UiText, val duration: Int) : UiEvent()
}
