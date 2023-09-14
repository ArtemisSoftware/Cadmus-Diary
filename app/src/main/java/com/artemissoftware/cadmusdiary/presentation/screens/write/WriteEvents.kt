package com.artemissoftware.cadmusdiary.presentation.screens.write

import com.artemissoftware.cadmusdiary.domain.model.Mood
import java.time.ZonedDateTime

sealed class WriteEvents {

    object PopBackStack : WriteEvents()

    data class SetTitle(val title: String) : WriteEvents()

    data class SetDescription(val description: String) : WriteEvents()

    data class SetMood(val mood: Mood) : WriteEvents()

    object SaveDiary : WriteEvents()

    data class UpdateDateTime(val zonedDateTime: ZonedDateTime) : WriteEvents()

    object DeleteDiary : WriteEvents()
}
