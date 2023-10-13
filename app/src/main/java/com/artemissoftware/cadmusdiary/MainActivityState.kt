package com.artemissoftware.cadmusdiary

data class MainActivityState(
    val keepSplashOpened: Boolean = true,
    val userLoggedIn: Boolean = false,
    val startDestination: String? = null,
)
