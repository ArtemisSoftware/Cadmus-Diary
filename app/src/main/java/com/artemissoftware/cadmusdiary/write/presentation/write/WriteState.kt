package com.artemissoftware.cadmusdiary.write.presentation.write

import com.artemissoftware.cadmusdiary.core.domain.models.Diary
import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.core.ui.mood.MoodUI
import com.artemissoftware.cadmusdiary.util.DateTimeConstants
import com.artemissoftware.cadmusdiary.util.extensions.toInstant
import io.realm.kotlin.types.RealmInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WriteState(
    val selectedDiaryId: String? = null,
    val selectedDiary: Diary? = null,
    val selectedImage: GalleryImage? = null,
    val title: String = "",
    val description: String = "",
    val mood: MoodUI = MoodUI.Neutral,
    val updatedDateTime: RealmInstant? = null,
    val galleryState: ImageGalleryState = ImageGalleryState(),
) {

    fun getSelectedDiaryDateTime(): String {
        return if (selectedDiary != null) {
            DateTimeFormatter.ofPattern(DateTimeConstants.FORMAT_dd_MMM_yyyy__hh_mm_a, Locale.getDefault())
                .withZone(ZoneId.systemDefault())
                .format(selectedDiary.date.toInstant())
        } else {
            "Unknown"
        }
    }

    fun getImagesUri() = galleryState.images.map { it.image }
}
