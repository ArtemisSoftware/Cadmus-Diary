package com.artemissoftware.cadmusdiary.data.mappers

import android.net.Uri
import com.artemissoftware.cadmusdiary.data.database.entity.ImageToUploadEntity
import com.artemissoftware.cadmusdiary.domain.model.Picture

fun Picture.toEntity(sessionUri: Uri): ImageToUploadEntity {
    return ImageToUploadEntity(
        remoteImagePath = remotePath,
        imageUri = image,
        sessionUri = sessionUri.toString(),
    )
}
