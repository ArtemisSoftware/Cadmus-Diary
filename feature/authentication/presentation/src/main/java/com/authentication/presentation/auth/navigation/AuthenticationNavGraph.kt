package com.authentication.presentation.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemissoftware.navigation.Screen
import com.authentication.presentation.auth.AuthenticationScreen

fun NavGraphBuilder.authenticationRoute(
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Authentication.route) {
        AuthenticationScreen(
            navController = navController,
            onDataLoaded = onDataLoaded,
        )
    }
}
