package com.artemissoftware.cadmusdiary.core.domain.models

data class DiaryImages(
    val id: String,
    val images: List<String> = emptyList(),
)
