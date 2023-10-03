package com.artemissoftware.cadmusdiary.write.presentation.write.mappers

import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.core.domain.models.Picture

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
