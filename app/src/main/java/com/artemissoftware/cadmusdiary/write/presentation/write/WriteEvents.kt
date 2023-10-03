package com.artemissoftware.cadmusdiary.write.presentation.write

import android.net.Uri
import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.core.ui.mood.MoodUI
import com.artemissoftware.cadmusdiary.domain.model.Mood
import java.time.ZonedDateTime

sealed class WriteEvents {

    object PopBackStack : WriteEvents()

    data class SetTitle(val title: String) : WriteEvents()

    data class SetDescription(val description: String) : WriteEvents()

    data class SetMood(val mood: MoodUI) : WriteEvents()

    object SaveDiary : WriteEvents()

    data class UpdateDateTime(val zonedDateTime: ZonedDateTime) : WriteEvents()

    object DeleteDiary : WriteEvents()

    data class AddImage(val image: Uri) : WriteEvents()

    data class ZoomInImage(val image: GalleryImage) : WriteEvents()

    object ZoomOutImage : WriteEvents()

    data class DeleteImage(val image: GalleryImage) : WriteEvents()
}
