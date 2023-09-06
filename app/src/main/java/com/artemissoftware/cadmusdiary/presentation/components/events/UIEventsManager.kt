package com.artemissoftware.cadmusdiary.presentation.components.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.artemissoftware.cadmusdiary.presentation.components.events.MessageBarType
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UIEventsManager(
    uiEvent: Flow<UiEvent>,
    navController: NavHostController,
    showMessageBar: (MessageBarType) -> Unit = {},
//    onNavigateAndPopCurrent: (UiEvent.NavigateAndPopCurrent) -> Unit = {},
//    onPopBackStackWithArguments: (UiEvent.PopBackStackWithArguments<*>) -> Unit = {},
//    onShowSnackBar: (TaskySnackBarType) -> Unit = {},
) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessageBar -> {
                    showMessageBar.invoke(event.messageBarType)
                }
                is UiEvent.PopBackStack -> { navController.popBackStack() }
//                is UiEvent.PopBackStackWithArguments<*> -> { onPopBackStackWithArguments(event) }
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
//                is UiEvent.NavigateAndPopCurrent -> {
//                    onNavigateAndPopCurrent(event)
//                }
//                is UiEvent.ShowSnackBar -> {
//                    onShowSnackBar.invoke(event.snackBarType)
//                }
                is UiEvent.NavigatePopCurrent -> {
                    navController.popBackStack()
                    navController.navigate(event.route)
                    //navController.popBackStack(route = event.route, inclusive = true)
                }
            }
        }
    }
}
