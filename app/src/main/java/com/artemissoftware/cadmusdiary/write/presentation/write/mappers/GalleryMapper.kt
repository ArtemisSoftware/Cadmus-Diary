package com.artemissoftware.cadmusdiary.write.presentation.write.mappers

import com.core.ui.models.GalleryImage
import com.core.domain.models.Picture

fun List<GalleryImage>.toPictures(): List<Picture> {
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
