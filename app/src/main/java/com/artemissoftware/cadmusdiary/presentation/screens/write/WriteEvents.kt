package com.artemissoftware.cadmusdiary.presentation.screens.write

import android.net.Uri
import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
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

    data class AddImage(val image: Uri, val remoteImagePath: String = "") : WriteEvents()

    data class ZoomInImage(val image: GalleryImage) : WriteEvents()

    object ZoomOutImage : WriteEvents()
}
