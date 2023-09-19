package com.artemissoftware.cadmusdiary.domain.model

data class DiaryImages(
    val id: String,
    val images: List<String> = emptyList(),
)
