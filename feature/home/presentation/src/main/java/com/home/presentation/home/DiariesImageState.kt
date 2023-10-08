package com.home.presentation.home

import android.net.Uri

data class DiariesImageState(
    val id: String,
    val isLoading: Boolean = false,
    val isOpened: Boolean = false,
    val uris: List<Uri> = emptyList(),
)
