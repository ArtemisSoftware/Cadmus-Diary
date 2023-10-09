package com.core.data.mappers

import android.net.Uri
import com.core.data.database.entity.ImageToUploadEntity
import com.core.domain.models.Picture

fun Picture.toEntity(sessionUri: Uri): ImageToUploadEntity {
    return ImageToUploadEntity(
        remoteImagePath = remotePath,
        imageUri = image,
        sessionUri = sessionUri.toString(),
    )
}
