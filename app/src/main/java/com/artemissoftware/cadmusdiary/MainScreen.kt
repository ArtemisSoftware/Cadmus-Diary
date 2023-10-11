package com.artemissoftware.cadmusdiary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.rememberNavController
import com.artemissoftware.cadmusdiary.navigation.NavGraph

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val isUserLoggedIn = viewModel.state.collectAsState().value

    val navController = rememberNavController()
    NavGraph(
        isUserLoggedIn = isUserLoggedIn.userLoggedIn,
        navController = navController,
        onDataLoaded = {
            viewModel.onTriggerEvent(MainActivityEvents.FinishSplash)
        },
    )
}
