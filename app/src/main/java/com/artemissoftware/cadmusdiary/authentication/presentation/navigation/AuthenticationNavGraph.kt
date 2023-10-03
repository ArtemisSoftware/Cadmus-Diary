package com.artemissoftware.cadmusdiary.authentication.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemissoftware.cadmusdiary.authentication.presentation.auth.AuthenticationScreen
import com.artemissoftware.cadmusdiary.navigation.Screen

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
