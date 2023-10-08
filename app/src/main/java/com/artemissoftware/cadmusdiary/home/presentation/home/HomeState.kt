package com.artemissoftware.cadmusdiary.home.presentation.home

import android.net.Uri
import com.artemissoftware.cadmusdiary.core.data.repository.Diaries
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.core.ui.connectivity.ConnectivityObserver
import com.home.presentation.home.DiariesImageState

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
