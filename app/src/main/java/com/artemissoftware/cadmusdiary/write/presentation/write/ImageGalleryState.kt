package com.artemissoftware.cadmusdiary.write.presentation.write

import com.core.ui.models.GalleryImage

data class ImageGalleryState(
    val images: List<GalleryImage> = emptyList(),
    val imagesToBeDeleted: List<GalleryImage> = emptyList(),
)
