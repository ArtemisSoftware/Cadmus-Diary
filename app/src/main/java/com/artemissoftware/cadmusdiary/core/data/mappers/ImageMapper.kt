package com.artemissoftware.cadmusdiary.core.data.mappers

import android.net.Uri
import com.core.data.database.entity.ImageToUploadEntity
import com.artemissoftware.cadmusdiary.core.domain.models.Picture

fun Picture.toEntity(sessionUri: Uri): com.core.data.database.entity.ImageToUploadEntity {
    return com.core.data.database.entity.ImageToUploadEntity(
        remoteImagePath = remotePath,
        imageUri = image,
        sessionUri = sessionUri.toString(),
    )
}
