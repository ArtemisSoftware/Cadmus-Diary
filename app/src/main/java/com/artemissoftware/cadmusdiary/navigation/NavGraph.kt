package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.authentication.presentation.auth.navigation.authenticationRoute
import com.home.presentation.home.navigation.homeRoute
import com.write.presentation.write.navigation.writeRoute

@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {
        authenticationRoute(
            navController = navController,
            onDataLoaded = onDataLoaded,
        )
        homeRoute(
            navController = navController,
            onDataLoaded = onDataLoaded,
        )
        writeRoute(
            navController = navController,
        )
    }
}
