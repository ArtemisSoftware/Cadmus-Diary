package com.artemissoftware.cadmusdiary.domain.model

import android.net.Uri

data class Picture(
    val image: Uri,
    val remotePath: String = "",
)
