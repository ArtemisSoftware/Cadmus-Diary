package com.artemissoftware.cadmusdiary.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.artemissoftware.cadmusdiary.home.presentation.home.HomeScreen
import com.artemissoftware.cadmusdiary.navigation.Screen

fun NavGraphBuilder.homeRoute(
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Home.route) {
        HomeScreen(
            navController = navController,
            onDataLoaded = onDataLoaded,
        )
    }
}
