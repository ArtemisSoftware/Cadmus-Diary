package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.artemissoftware.cadmusdiary.authentication.presentation.navigation.authenticationRoute
import com.artemissoftware.cadmusdiary.home.presentation.navigation.homeRoute
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import com.artemissoftware.cadmusdiary.write.presentation.navigation.writeRoute
import io.realm.kotlin.mongodb.App

@Composable
fun NavGraph(
    startDestination: String = getStartDestination(),
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

private fun getStartDestination(): String {
    val user = App.create(APP_ID).currentUser
    return if (user != null && user.loggedIn) {
        Screen.Home.route
    } else {
        Screen.Authentication.route
    }
}
