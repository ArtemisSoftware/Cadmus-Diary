package com.artemissoftware.cadmusdiary.util.extensions

import android.content.Context
import com.artemissoftware.cadmusdiary.presentation.components.events.MessageBarType
import com.stevdzasan.messagebar.MessageBarState

fun MessageBarState.show(context: Context, messageBarType: MessageBarType) {
    when (messageBarType) {
        is MessageBarType.Error -> {
            addError(messageBarType.exception)
        }
        is MessageBarType.Success -> {
            addSuccess(messageBarType.description.asString(context))
        }
    }
}
