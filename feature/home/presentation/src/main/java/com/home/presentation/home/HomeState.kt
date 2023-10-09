package com.home.presentation.home

import android.net.Uri
import com.core.domain.RequestState
import com.core.domain.repository.Diaries
import com.core.ui.connectivity.ConnectivityObserver

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
