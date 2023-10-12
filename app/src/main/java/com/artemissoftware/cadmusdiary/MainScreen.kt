package com.artemissoftware.cadmusdiary

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.artemissoftware.cadmusdiary.navigation.NavGraph

@Composable
fun MainScreen(viewModel: MainActivityViewModel) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    val navController = rememberNavController()

    state.startDestination?.let { startDestination ->
        NavGraph(
            startDestination = startDestination,
            navController = navController,
            onDataLoaded = {
                viewModel.onTriggerEvent(MainActivityEvents.FinishSplash)
            },
        )
    }
}
