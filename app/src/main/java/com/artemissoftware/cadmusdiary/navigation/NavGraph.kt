package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artemissoftware.cadmusdiary.presentation.screens.auth.AuthenticationScreen
import com.artemissoftware.cadmusdiary.presentation.screens.home.HomeScreen
import com.artemissoftware.cadmusdiary.presentation.screens.write.WriteScreen
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
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

private fun NavGraphBuilder.authenticationRoute(
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

fun NavGraphBuilder.writeRoute(
    navController: NavHostController,
) {
    composable(
        route = Screen.Write.route,
        arguments = listOf(
            navArgument(name = Screen.WRITE_SCREEN_ARGUMENT_KEY) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
        ),
    ) {
        WriteScreen(
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
