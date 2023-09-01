package com.artemissoftware.cadmusdiary.presentation.screens.auth

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.artemissoftware.cadmusdiary.presentation.screens.auth.composables.AuthenticationContent
import com.artemissoftware.cadmusdiary.ui.theme.CadmusDiaryTheme
import com.artemissoftware.cadmusdiary.util.Constants.CLIENT_ID
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticationScreen(
//    authenticated: Boolean,

//
//    messageBarState: MessageBarState,
//
//    onSuccessfulFirebaseSignIn: (String) -> Unit,
//    onFailedFirebaseSignIn: (Exception) -> Unit,
//    onDialogDismissed: (String) -> Unit,
//    navigateToHome: () -> Unit
) {
    val oneTapState = rememberOneTapSignInState()

    AuthenticationScreenContent(
        isLoading = oneTapState.opened,
        onGoogleAuthenticationButtonClicked = {
            oneTapState.open()
        },
    )

    OneTapSignInWithGoogle(
        state = oneTapState,
        clientId = CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.d("Auth", tokenId)
//            val credential = GoogleAuthProvider.getCredential(tokenId, null)
//            FirebaseAuth.getInstance().signInWithCredential(credential)
//                .addOnCompleteListener { task ->
//                    if(task.isSuccessful) {
//                        onSuccessfulFirebaseSignIn(tokenId)
//                    } else {
//                        task.exception?.let { it -> onFailedFirebaseSignIn(it) }
//                    }
//                }
        },
        onDialogDismissed = { message ->
            Log.d("Auth", message)
//            onDialogDismissed(message)
        },
    )
}

@ExperimentalMaterial3Api
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun AuthenticationScreenContent(
    isLoading: Boolean,
    onGoogleAuthenticationButtonClicked: () -> Unit,
) {
    val messageBarState = rememberMessageBarState()

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(),
        content = {
            ContentWithMessageBar(messageBarState = messageBarState) {
                AuthenticationContent(
                    isLoading = isLoading,
                    onGoogleAuthenticationButtonClicked = onGoogleAuthenticationButtonClicked,
                )
            }
        },
    )

//    LaunchedEffect(key1 = authenticated) {
//        if (authenticated) {
//            navigateToHome()
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(name = "Light mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark mode")
private fun AuthenticationScreenContentPreview() {
    CadmusDiaryTheme {
        AuthenticationScreenContent(
            isLoading = false,
            onGoogleAuthenticationButtonClicked = {},
        )
    }
}
