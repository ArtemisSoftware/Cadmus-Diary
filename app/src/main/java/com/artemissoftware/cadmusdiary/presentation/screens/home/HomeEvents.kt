package com.artemissoftware.cadmusdiary.presentation.screens.home

import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime

sealed class HomeEvents {

    object OpenSignOutDialog : HomeEvents()
    object CloseSignOutDialog : HomeEvents()

    object SignOutGoogleAccount : HomeEvents()

    data class Navigate(val route: String) : HomeEvents()

    data class FetchImages(val diaryId: ObjectId, val list: List<String>) : HomeEvents()
    data class OpenDiaryGallery(val diaryId: ObjectId) : HomeEvents()

    object OpenDeleteAllDialog : HomeEvents()
    object CloseDeleteAllDialog : HomeEvents()

    object DeleteAllDiaries : HomeEvents()

    data class GetDiaries(val zonedDateTime: ZonedDateTime? = null) : HomeEvents()
}
