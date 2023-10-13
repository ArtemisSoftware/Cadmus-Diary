package com.core.ui.util

import java.lang.Exception

sealed class MessageBarType {
    data class Success(val description: UiText) : MessageBarType()

    data class Error(val exception: Exception) : MessageBarType()
}
