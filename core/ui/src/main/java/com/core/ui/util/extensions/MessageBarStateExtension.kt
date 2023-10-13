package com.core.ui.util.extensions

import android.content.Context
import com.core.ui.util.MessageBarType
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
