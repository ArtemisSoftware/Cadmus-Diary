package com.write.presentation.write

import com.core.ui.models.GalleryImage

internal data class ImageGalleryState(
    val images: List<GalleryImage> = emptyList(),
    val imagesToBeDeleted: List<GalleryImage> = emptyList(),
)
