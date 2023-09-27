package com.artemissoftware.cadmusdiary.core.domain.models

data class ImageResult(
    val urls: List<String>,
    val numberOfErrors: Int = 0
)
