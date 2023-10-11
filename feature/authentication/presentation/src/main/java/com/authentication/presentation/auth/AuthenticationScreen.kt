package com.authentication.presentation.auth

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.authentication.presentation.auth.Const.CLIENT_ID
import com.authentication.presentation.auth.composables.AuthenticationContent
import com.core.ui.components.events.UIEventsManager
import com.core.ui.components.modifier.statusNavigationBarPadding
import com.core.ui.theme.CadmusDiaryTheme
import com.core.ui.util.MessageBarType
import com.core.ui.util.extensions.show
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState().value
    val oneTapState = rememberOneTapSignInState()
    val messageBarState = rememberMessageBarState()

    Scaffold(
        modifier = Modifier.statusNavigationBarPadding(),
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationScreenContent(
                    isLoading = state.isLoading,
                    onGoogleAuthenticationButtonClicked = {
                        oneTapState.open()
                        viewModel.onTriggerEvent(AuthenticationEvents.SetLoading(true))
                    },
                )
            }
        },
    )

    UIEventsManager(
        uiEvent = viewModel.uiEvent,
        navController = navController,
        showMessageBar = {
            messageBarState.show(context = context, messageBarType = it)
        },
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            viewModel.onTriggerEvent(AuthenticationEvents.SignIn(tokenId = tokenId))
        },
        onDialogDismissed = { message ->
            messageBarState.show(context = context, messageBarType = MessageBarType.Error(Exception(message)))
        },
    )

    LaunchedEffect(key1 = Unit) {
        onDataLoaded()
    }
}

@ExperimentalMaterial3Api
@Composable
private fun AuthenticationScreenContent(
    isLoading: Boolean,
    onGoogleAuthenticationButtonClicked: () -> Unit,
) {
    AuthenticationContent(
        isLoading = isLoading,
        onGoogleAuthenticationButtonClicked = onGoogleAuthenticationButtonClicked,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark mode")
private fun AuthenticationScreenContentPreview() {
    CadmusDiaryTheme {
        Surface(
            content = {
                AuthenticationScreenContent(
                    isLoading = false,
                    onGoogleAuthenticationButtonClicked = {},
                )
            },
        )
    }
}
