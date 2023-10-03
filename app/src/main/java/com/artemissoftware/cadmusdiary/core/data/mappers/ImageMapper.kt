package com.artemissoftware.cadmusdiary.core.data.mappers

import android.net.Uri
import com.artemissoftware.cadmusdiary.core.data.database.entity.ImageToUploadEntity
import com.artemissoftware.cadmusdiary.core.domain.models.Picture

fun Picture.toEntity(sessionUri: Uri): ImageToUploadEntity {
    return ImageToUploadEntity(
        remoteImagePath = remotePath,
        imageUri = image,
        sessionUri = sessionUri.toString(),
    )
}
