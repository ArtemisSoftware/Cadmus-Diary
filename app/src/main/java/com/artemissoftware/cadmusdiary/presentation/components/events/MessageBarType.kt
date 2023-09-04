package com.artemissoftware.cadmusdiary.presentation.components.events

import com.artemissoftware.cadmusdiary.util.UiText
import java.lang.Exception

sealed class MessageBarType {
    data class Success(val description: UiText) : MessageBarType()

    data class Error(val exception: Exception) : MessageBarType()

}
