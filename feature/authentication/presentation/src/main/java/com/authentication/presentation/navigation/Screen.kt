package com.authentication.presentation.navigation

internal sealed class Screen(val route: String) {
    object Authentication : Screen(route = "authentication")
}
