package com.home.presentation.home.navigation

internal sealed class Screen(val route: String) {
    object Home : Screen(route = "home")
}
