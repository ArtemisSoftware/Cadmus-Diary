package com.artemissoftware.cadmusdiary.presentation.screens.home

sealed class HomeEvents {

    object OpenSignOutDialog : HomeEvents()
    object CloseSignOutDialog : HomeEvents()

    object SignOutGoogleAccount : HomeEvents()
}
