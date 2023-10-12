package com.write.presentation.write.mappers

import com.core.domain.models.Picture
import com.core.ui.models.GalleryImage

internal fun List<GalleryImage>.toPictures(): List<Picture> {
    val list = mutableListOf<Picture>()

    this.forEach { galleryImage ->
        list.add(
            Picture(
                image = galleryImage.image.toString(),
                remotePath = galleryImage.remoteImagePath,
            ),
        )
    }

    return list
}
