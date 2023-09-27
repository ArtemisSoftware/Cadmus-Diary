package com.artemissoftware.cadmusdiary.presentation.screens.home

import android.net.Uri
import com.artemissoftware.cadmusdiary.core.ui.connectivity.ConnectivityObserver
import com.artemissoftware.cadmusdiary.data.repository.Diaries
import com.artemissoftware.cadmusdiary.domain.RequestState

data class HomeState(
    val signOutDialogOpened: Boolean = false,
    val deleteAllDialogOpened: Boolean = false,
    val diaries: Diaries = RequestState.Idle,
    val network: ConnectivityObserver.Status = ConnectivityObserver.Status.Unavailable,
    val diariesImages: List<DiariesImageState> = emptyList(),
    val dateIsSelected: Boolean = false,
) {
    fun isFetchingImages(id: String): Boolean = with(diariesImages) {
        return find { it.id == id }?.isLoading ?: false
    }

    fun isGalleryOpen(id: String): Boolean = with(diariesImages) {
        return find { it.id == id }?.isOpened ?: false
    }

    fun downloadedImages(id: String): List<Uri> = with(diariesImages) {
        return find { it.id == id }?.uris ?: emptyList()
    }
}
