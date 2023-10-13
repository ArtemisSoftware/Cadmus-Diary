package com.home.presentation.home

import java.time.ZonedDateTime

internal sealed class HomeEvents {

    object OpenSignOutDialog : HomeEvents()
    object CloseSignOutDialog : HomeEvents()

    object SignOutGoogleAccount : HomeEvents()

    data class Navigate(val route: String) : HomeEvents()

    data class FetchImages(val diaryId: String, val list: List<String>) : HomeEvents()
    data class OpenDiaryGallery(val diaryId: String) : HomeEvents()
    data class CloseDiaryGallery(val diaryId: String) : HomeEvents()

    object OpenDeleteAllDialog : HomeEvents()
    object CloseDeleteAllDialog : HomeEvents()

    object DeleteAllDiaries : HomeEvents()

    data class GetDiaries(val zonedDateTime: ZonedDateTime? = null) : HomeEvents()
}
