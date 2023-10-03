package com.artemissoftware.cadmusdiary.write.presentation.write

import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage

data class ImageGalleryState(
    val images: List<GalleryImage> = emptyList(),
    val imagesToBeDeleted: List<GalleryImage> = emptyList(),
)
