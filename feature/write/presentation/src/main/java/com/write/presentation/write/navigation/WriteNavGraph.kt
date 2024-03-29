package com.write.presentation.write.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artemissoftware.navigation.Screen
import com.write.presentation.write.WriteScreen

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
