package com.home.presentation.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.artemissoftware.navigation.Screen
import com.home.presentation.home.HomeScreen

// private const val INTERESTS_GRAPH_ROUTE_PATTERN = "interests_graph"
// const val interestsRoute = "interests_route"

// fun NavController.navigateToInterestsGraph() {
//    this.navigate(INTERESTS_GRAPH_ROUTE_PATTERN)
// }
//
// fun NavGraphBuilder.interestsGraph(
//    navController: NavHostController,
//    onDataLoaded: () -> Unit,
// ) {
//    navigation(
//        route = INTERESTS_GRAPH_ROUTE_PATTERN,
//        startDestination = interestsRoute,
//    ) {
//        composable(route = Screen.Home.route) {
//            HomeScreen(
//                navController = navController,
//                onDataLoaded = onDataLoaded,
//            )
//        }
//    }
// }
//

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
