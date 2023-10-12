package com.write.presentation.write

import com.artemissoftware.util.DateTimeConstants
import com.core.domain.models.JournalEntry
import com.core.ui.models.GalleryImage
import com.core.ui.models.MoodUI
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WriteState(
    val selectedDiaryId: String? = null,
    val selectedDiary: JournalEntry? = null,
    val selectedImage: GalleryImage? = null,
    val title: String = "",
    val description: String = "",
    val mood: MoodUI = MoodUI.Neutral,
    val updatedDateTime: Instant? = null,
    val galleryState: ImageGalleryState = ImageGalleryState(),
) {

    fun getSelectedDiaryDateTime(): String {
        return if (selectedDiary != null) {
            DateTimeFormatter.ofPattern(DateTimeConstants.FORMAT_dd_MMM_yyyy__hh_mm_a, Locale.getDefault())
                .withZone(ZoneId.systemDefault())
                .format(selectedDiary.date)
        } else {
            "Unknown"
        }
    }

    fun getImagesUri() = galleryState.images.map { it.image }
}
