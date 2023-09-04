package com.artemissoftware.cadmusdiary.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.artemissoftware.cadmusdiary.presentation.components.events.MessageBarType
import com.artemissoftware.cadmusdiary.presentation.components.events.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UIEventsManager(
    uiEvent: Flow<UiEvent>,
    showMessageBar: (MessageBarType) -> Unit = {},
//    onNavigate: (UiEvent.Navigate) -> Unit = {},
//    onNavigateAndPopCurrent: (UiEvent.NavigateAndPopCurrent) -> Unit = {},
//    onPopBackStack: () -> Unit = {},
//    onPopBackStackWithArguments: (UiEvent.PopBackStackWithArguments<*>) -> Unit = {},
//    onShowSnackBar: (TaskySnackBarType) -> Unit = {},
) {
    LaunchedEffect(key1 = Unit) {
        uiEvent.collectLatest { event ->
            when (event) {
                is UiEvent.ShowMessageBar -> {
                    showMessageBar.invoke(event.messageBarType)
                }
//                is UiEvent.PopBackStack -> { onPopBackStack.invoke() }
//                is UiEvent.PopBackStackWithArguments<*> -> { onPopBackStackWithArguments(event) }
//                is UiEvent.Navigate -> { onNavigate(event) }
//                is UiEvent.NavigateAndPopCurrent -> {
//                    onNavigateAndPopCurrent(event)
//                }
//                is UiEvent.ShowSnackBar -> {
//                    onShowSnackBar.invoke(event.snackBarType)
//                }
            }
        }
    }
}
