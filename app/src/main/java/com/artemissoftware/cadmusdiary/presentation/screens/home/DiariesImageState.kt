package com.artemissoftware.cadmusdiary.presentation.screens.home

import android.net.Uri

data class DiariesImageState(
    val id: String,
    val isLoading: Boolean = false,
    val uris: List<Uri> = emptyList(),
)
