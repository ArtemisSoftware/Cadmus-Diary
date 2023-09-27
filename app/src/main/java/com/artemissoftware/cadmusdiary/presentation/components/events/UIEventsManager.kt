package com.artemissoftware.cadmusdiary.presentation.components.events

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UIEventsManager(
    uiEvent: Flow<UiEvent>,
    navController: NavHostController,
    context: Context = LocalContext.current,
    showMessageBar: (MessageBarType) -> Unit = {},
    closeNavigationDrawer: () -> Unit = {},
) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessageBar -> {
                    showMessageBar.invoke(event.messageBarType)
                }
                is UiEvent.PopBackStack -> { navController.popBackStack() }
                is UiEvent.Navigate -> {
                    navController.navigate(event.route)
                }
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.uiText.asString(context), event.duration).show()
                }
                is UiEvent.NavigatePopCurrent -> {
                    navController.popBackStack()
                    navController.navigate(event.route)
                }

                UiEvent.CloseNavigationDrawer -> closeNavigationDrawer.invoke()
            }
        }
    }
}
