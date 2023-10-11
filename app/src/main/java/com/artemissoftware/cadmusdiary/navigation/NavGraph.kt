package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.artemissoftware.navigation.Screen
import com.home.presentation.home.navigation.homeRoute

//import com.authentication.presentation.navigation.authenticationRoute
//import com.home.presentation.home.navigation.homeRoute
//import com.write.presentation.write.navigation.writeRoute

@Composable
fun NavGraph(
    isUserLoggedIn: Boolean,
    startDestination: String = getStartDestination(isUserLoggedIn),
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {
//        authenticationRoute(
//            navController = navController,
//            onDataLoaded = onDataLoaded,
//        )
        homeRoute(
            navController = navController,
            onDataLoaded = onDataLoaded,
        )
//        writeRoute(
//            navController = navController,
//        )
    }
}

private fun getStartDestination(isUserLoggedIn: Boolean): String {
    return if (isUserLoggedIn) {
        Screen.Home.route
    } else {
        Screen.Home.route
        //Screen.Authentication.route
    }
}
