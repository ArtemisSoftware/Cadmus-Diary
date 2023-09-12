package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.artemissoftware.cadmusdiary.presentation.screens.auth.AuthenticationScreen
import com.artemissoftware.cadmusdiary.presentation.screens.auth.AuthenticationViewModel
import com.artemissoftware.cadmusdiary.presentation.screens.home.HomeScreen
import com.artemissoftware.cadmusdiary.presentation.screens.home.HomeViewModel
import com.artemissoftware.cadmusdiary.presentation.screens.write.WriteScreen
import com.artemissoftware.cadmusdiary.presentation.screens.write.WriteViewModel
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
//            navigateToHome = {
//                navController.popBackStack()
//                navController.navigate(Screen.Home.route)
//            },

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

fun NavGraphBuilder.authenticationRoute(
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    composable(route = Screen.Authentication.route) {
        val viewModel: AuthenticationViewModel = viewModel()

        AuthenticationScreen(
            viewModel = viewModel,
            navController = navController,
            onDataLoaded = onDataLoaded,
//            onSuccessfulFirebaseSignIn = { tokenId ->
//                viewModel.signInWithMongoAtlas(
//                    tokenId = tokenId,
//                    onSuccess = {
//                        messageBarState.addSuccess("Successfully Authenticated!")
//                        viewModel.setLoading(false)
//                    },
//                    onError = {
//                        messageBarState.addError(it)
//                        viewModel.setLoading(false)
//                    }
//                )
//            },
//            onFailedFirebaseSignIn = {
//                messageBarState.addError(it)
//                viewModel.setLoading(false)
//            },
//            onDialogDismissed = { message ->
//                messageBarState.addError(Exception(message))
//                viewModel.setLoading(false)
//            }
        )
    }
}

fun NavGraphBuilder.homeRoute(
    navController: NavHostController,
    onDataLoaded: () -> Unit,

) {
    composable(route = Screen.Home.route) {
//        val viewModel: HomeViewModel = hiltViewModel()
        val viewModel: HomeViewModel = viewModel()

//        val context = LocalContext.current
//        var signOutDialogOpened by remember { mutableStateOf(false) }
//        var deleteAllDialogOpened by remember { mutableStateOf(false) }
//

//
        HomeScreen(
            viewModel = viewModel,
            navController = navController,
            onDataLoaded = onDataLoaded,
//            dateIsSelected = viewModel.dateIsSelected,
//            onDateSelected = { viewModel.getDiaries(zonedDateTime = it) },
//            onDateReset = { viewModel.getDiaries() },
//            onSignOutClicked = { signOutDialogOpened = true },
//            onDeleteAllClicked = { deleteAllDialogOpened = true },
        )

//
//        DisplayAlertDialog(
//            title = "Delete All Diaries",
//            message = "Are you sure you want to permanently delete all your diaries?",
//            dialogOpened = deleteAllDialogOpened,
//            onDialogClosed = { deleteAllDialogOpened = false },
//            onYesClicked = {
//                viewModel.deleteAllDiaries(
//                    onSuccess = {
//                        Toast.makeText(
//                            context,
//                            "All Diaries Deleted.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    },
//                    onError = {
//                        Toast.makeText(
//                            context,
//                            if (it.message == "No Internet Connection.")
//                                "We need an Internet Connection for this operation."
//                            else it.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        scope.launch {
//                            drawerState.close()
//                        }
//                    }
//                )
//            }
//        )
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
        val viewModel: WriteViewModel = viewModel() // hiltViewModel()
//        val context = LocalContext.current
//        val galleryState = viewModel.galleryState
//        val pagerState = rememberPagerState()
//        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
//
        WriteScreen(
            viewModel = viewModel,
            navController = navController,
//            pagerState = pagerState,
//            galleryState = galleryState,
//            moodName = { Mood.values()[pageNumber].name },
//            onTitleChanged = { viewModel.setTitle(title = it) },
//            onDescriptionChanged = { viewModel.setDescription(description = it) },
//            onDateTimeUpdated = { viewModel.updateDateTime(zonedDateTime = it) },
//            onBackPressed = navigateBack,
//            onDeleteConfirmed = {
//                viewModel.deleteDiary(
//                    onSuccess = {
//                        Toast.makeText(
//                            context,
//                            "Deleted",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        navigateBack()
//                    },
//                    onError = { message ->
//                        Toast.makeText(
//                            context,
//                            message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                )
//            },
//            onSaveClicked = {
//                viewModel.upsertDiary(
//                    diary = it.apply { mood = Mood.values()[pageNumber].name },
//                    onSuccess = navigateBack,
//                    onError = { message ->
//                        Toast.makeText(
//                            context,
//                            message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                )
//            },
//            onImageSelect = {
//                val type = context.contentResolver.getType(it)?.split("/")?.last() ?: "jpg"
//                viewModel.addImage(image = it, imageType = type)
//            },
//            onImageDeleteClicked = { galleryState.removeImage(it) }
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
