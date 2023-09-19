package com.artemissoftware.cadmusdiary.presentation.screens.home

import android.net.Uri
import com.artemissoftware.cadmusdiary.data.repository.Diaries
import com.artemissoftware.cadmusdiary.domain.RequestState

data class HomeState(
    val signOutDialogOpened: Boolean = false,
    val diaries: Diaries = RequestState.Idle,
    val diariesImages: List<DiariesImageState> = emptyList(),
) {
    fun isFetchingImages(id: String): Boolean {
        return diariesImages.find { it.id == id }?.isLoading ?: false
    }

    fun downloadedImages(id: String): List<Uri> {
        return diariesImages.find { it.id == id }?.uris ?: emptyList()
    }
}
