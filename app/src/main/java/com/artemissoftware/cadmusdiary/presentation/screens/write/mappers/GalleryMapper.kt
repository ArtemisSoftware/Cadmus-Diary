package com.artemissoftware.cadmusdiary.presentation.screens.write.mappers

import com.artemissoftware.cadmusdiary.core.ui.gallery.GalleryImage
import com.artemissoftware.cadmusdiary.domain.model.Picture

// TODO: rever isto
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
