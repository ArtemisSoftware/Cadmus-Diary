package com.artemissoftware.cadmusdiary.home.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.core.ui.components.dialog.DisplayAlertDialog
import com.core.ui.components.events.UIEventsManager
import com.home.presentation.home.composables.EmptyPage
import com.home.presentation.home.composables.HomeContent
import com.home.presentation.home.HomeEvents
import com.home.presentation.home.HomeState
import com.home.presentation.home.composables.HomeTopBar
import com.home.presentation.home.composables.NavigationDrawer
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavHostController,
    onDataLoaded: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val state = viewModel.state.collectAsState().value

    NavigationDrawer(
        drawerState = drawerState,
        onSignOutClicked = {
            viewModel.onTriggerEvent(HomeEvents.OpenSignOutDialog)
        },
        onDeleteAllClicked = {
            viewModel.onTriggerEvent(HomeEvents.OpenDeleteAllDialog)
        },
    ) {
        HomeScreenContent(
            state = state,
            events = viewModel::onTriggerEvent,
            onMenuClicked = {
                scope.launch {
                    drawerState.open()
                }
            },
        )
    }

    UIEventsManager(
        uiEvent = viewModel.uiEvent,
        navController = navController,
        closeNavigationDrawer = {
            scope.launch {
                drawerState.close()
            }
        },
    )

    DisplayAlertDialog(
        title = stringResource(id = R.string.sign_out),
        message = stringResource(id = R.string.are_you_sure_sign_out_google_account),
        dialogOpened = state.signOutDialogOpened,
        onDialogClosed = {
            viewModel.onTriggerEvent(HomeEvents.CloseSignOutDialog)
        },
        onYesClicked = {
            viewModel.onTriggerEvent(HomeEvents.SignOutGoogleAccount)
        },
    )

    DisplayAlertDialog(
        title = stringResource(R.string.delete_all_diaries),
        message = stringResource(R.string.are_you_sure_you_want_to_permanently_delete_all_your_diaries),
        dialogOpened = state.deleteAllDialogOpened,
        onDialogClosed = {
            viewModel.onTriggerEvent(HomeEvents.CloseDeleteAllDialog)
        },
        onYesClicked = {
            viewModel.onTriggerEvent(HomeEvents.DeleteAllDiaries)
        },
    )

    LaunchedEffect(key1 = state.diaries) {
        if (state.diaries !is com.core.domain.RequestState.Loading) {
            onDataLoaded()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun HomeScreenContent(
    onMenuClicked: () -> Unit,
    state: HomeState,
    events: (HomeEvents) -> Unit,
//    dateIsSelected: Boolean,
//    onDateSelected: (ZonedDateTime) -> Unit,
//    onDateReset: () -> Unit,
//    navigateToWrite: () -> Unit,
//    navigateToWriteWithArgs: (String) -> Unit
) {
    var padding by remember { mutableStateOf(PaddingValues()) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                onMenuClicked = onMenuClicked,
                dateIsSelected = state.dateIsSelected,
                onDateSelected = {
                    events.invoke(HomeEvents.GetDiaries(it))
                },
                onDateReset = {
                    events.invoke(HomeEvents.GetDiaries())
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(
                    end = padding.calculateEndPadding(LayoutDirection.Ltr),
                ),
                onClick = {
                    events.invoke(HomeEvents.Navigate(Screen.Write.route))
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "New Diary Icon",
                )
            }
        },
        content = {
            padding = it
            when (state.diaries) {
                is com.core.domain.RequestState.Success -> {
                    HomeContent(
                        paddingValues = it,
                        state = state,
                        diaryNotes = state.diaries.data,
                        onClick = { diaryId ->
                            events.invoke(HomeEvents.Navigate(Screen.Write.passDiaryId(diaryId = diaryId)))
                        },
                        openGallery = { diaryId ->
                            events.invoke(HomeEvents.OpenDiaryGallery(diaryId = diaryId.toString()))
                        },
                        fetchImages = { id, list ->
                            events.invoke(HomeEvents.FetchImages(diaryId = id.toString(), list))
                        },
                    )
                }
                is com.core.domain.RequestState.Error -> {
                    EmptyPage(
                        title = R.string.error,
                        subtitle = "${state.diaries.error.message}",
                    )
                }
                is com.core.domain.RequestState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else -> Unit
            }
        },
    )
}

@Composable
@Preview
private fun HomeTopBarPreview() {
    HomeScreenContent(
        onMenuClicked = {},
        state = HomeState(),
        events = {},
    )
}
