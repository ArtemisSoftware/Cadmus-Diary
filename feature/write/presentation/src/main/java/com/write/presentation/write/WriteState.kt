package com.write.presentation.write

import com.core.domain.models.Diary
import com.core.ui.models.GalleryImage
import com.core.ui.models.MoodUI
import com.artemissoftware.cadmusdiary.util.extensions.toInstant
import com.core.ui.util.DateTimeConstants
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
