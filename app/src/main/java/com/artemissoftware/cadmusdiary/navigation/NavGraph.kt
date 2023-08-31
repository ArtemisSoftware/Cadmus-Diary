package com.artemissoftware.cadmusdiary.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController,
//    onDataLoaded: () -> Unit
) {
    NavHost(
        startDestination = startDestination,
        navController = navController,
    ) {
        authenticationRoute(
//            navigateToHome = {
//                navController.popBackStack()
//                navController.navigate(Screen.Home.route)
//            },
//            onDataLoaded = onDataLoaded
        )
        homeRoute(
//            navigateToWrite = {
//                navController.navigate(Screen.Write.route)
//            },
//            navigateToWriteWithArgs = {
//                navController.navigate(Screen.Write.passDiaryId(diaryId = it))
//            },
//            navigateToAuth = {
//                navController.popBackStack()
//                navController.navigate(Screen.Authentication.route)
//            },
//            onDataLoaded = onDataLoaded
        )
        writeRoute(
//            navigateBack = {
//                navController.popBackStack()
//            }
        )
    }
}

fun NavGraphBuilder.authenticationRoute(
//    navigateToHome: () -> Unit,
//    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Authentication.route) {
//        val viewModel: AuthenticationViewModel = viewModel()
//        val authenticated by viewModel.authenticated
//        val loadingState by viewModel.loadingState
//        val oneTapState = rememberOneTapSignInState()
//        val messageBarState = rememberMessageBarState()
//
//        LaunchedEffect(key1 = Unit) {
//            onDataLoaded()
//        }
//
//        AuthenticationScreen(
//            authenticated = authenticated,
//            loadingState = loadingState,
//            oneTapState = oneTapState,
//            messageBarState = messageBarState,
//            onButtonClicked = {
//                oneTapState.open()
//                viewModel.setLoading(true)
//            },
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
//            },
//            navigateToHome = navigateToHome
//        )
    }
}

fun NavGraphBuilder.homeRoute(
//    navigateToWrite: () -> Unit,
//    navigateToWriteWithArgs: (String) -> Unit,
//    navigateToAuth: () -> Unit,
//    onDataLoaded: () -> Unit
) {
    composable(route = Screen.Home.route) {
//        val viewModel: HomeViewModel = hiltViewModel()
//        val diaries by viewModel.diaries
//        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//        val scope = rememberCoroutineScope()
//        val context = LocalContext.current
//        var signOutDialogOpened by remember { mutableStateOf(false) }
//        var deleteAllDialogOpened by remember { mutableStateOf(false) }
//
//        LaunchedEffect(key1 = diaries) {
//            if (diaries !is RequestState.Loading) {
//                onDataLoaded()
//            }
//        }
//
//        HomeScreen(
//            diaries = diaries,
//            drawerState = drawerState,
//            onMenuClicked = {
//                scope.launch {
//                    drawerState.open()
//                }
//            },
//            dateIsSelected = viewModel.dateIsSelected,
//            onDateSelected = { viewModel.getDiaries(zonedDateTime = it) },
//            onDateReset = { viewModel.getDiaries() },
//            onSignOutClicked = { signOutDialogOpened = true },
//            onDeleteAllClicked = { deleteAllDialogOpened = true },
//            navigateToWrite = navigateToWrite,
//            navigateToWriteWithArgs = navigateToWriteWithArgs
//        )
//
//        DisplayAlertDialog(
//            title = "Sign Out",
//            message = "Are you sure you want to Sign Out from your Google Account?",
//            dialogOpened = signOutDialogOpened,
//            onDialogClosed = { signOutDialogOpened = false },
//            onYesClicked = {
//                scope.launch(Dispatchers.IO) {
//                    val user = App.create(APP_ID).currentUser
//                    if (user != null) {
//                        user.logOut()
//                        withContext(Dispatchers.Main) {
//                            navigateToAuth()
//                        }
//                    }
//                }
//            }
//        )
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
//    navigateBack: () -> Unit
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
//        val viewModel: WriteViewModel = hiltViewModel()
//        val context = LocalContext.current
//        val uiState = viewModel.uiState
//        val galleryState = viewModel.galleryState
//        val pagerState = rememberPagerState()
//        val pageNumber by remember { derivedStateOf { pagerState.currentPage } }
//
//        WriteScreen(
//            uiState = uiState,
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
//        )
    }
}