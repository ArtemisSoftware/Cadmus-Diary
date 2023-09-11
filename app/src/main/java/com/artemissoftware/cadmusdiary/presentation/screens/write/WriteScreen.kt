package com.artemissoftware.cadmusdiary.presentation.screens.write

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.artemissoftware.cadmusdiary.presentation.components.events.UIEventsManager
import com.artemissoftware.cadmusdiary.presentation.screens.write.composables.WriteContent
import com.artemissoftware.cadmusdiary.presentation.screens.write.composables.WriteTopBar

@Composable
fun WriteScreen(
    viewModel: WriteViewModel,
    navController: NavHostController,
//    uiState: UiState,
//    pagerState: PagerState,
//    galleryState: GalleryState,
//    moodName: () -> String,
//    onTitleChanged: (String) -> Unit,
//    onDescriptionChanged: (String) -> Unit,
//    onDeleteConfirmed: () -> Unit,
//    onDateTimeUpdated: (ZonedDateTime) -> Unit,
//    onBackPressed: () -> Unit,
//    onSaveClicked: (Diary) -> Unit,
//    onImageSelect: (Uri) -> Unit,
//    onImageDeleteClicked: (GalleryImage) -> Unit
) {
//    var selectedGalleryImage by remember { mutableStateOf<GalleryImage?>(null) }
//    // Update the Mood when selecting an existing Diary
//    LaunchedEffect(key1 = uiState.mood) {
//        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
//    }

    val state = viewModel.state.collectAsState().value

    WriteScreenContent(
        state = state,
        events = {},
    )

    UIEventsManager(
        uiEvent = viewModel.uiEvent,
        navController = navController,
    )
}

@Composable
fun WriteScreenContent(
    events: (WriteEvents) -> Unit,
    state: WriteState,
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = /*uiState.selectedDiary*/ null,
//                moodName = moodName,
                onDeleteConfirmed = /*onDeleteConfirmed*/ {},
                onBackPressed = { events.invoke(WriteEvents.PopBackStack) },
//                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = { paddingValues ->
            WriteContent(
//                uiState = uiState,
//                pagerState = pagerState,
//                galleryState = galleryState,
                title = "uiState.title",
                onTitleChanged = { /*onTitleChanged*/ },
                description = "uiState.description",
                onDescriptionChanged = { /*onDescriptionChanged*/ },
                paddingValues = paddingValues,
//                onSaveClicked = onSaveClicked,
//                onImageSelect = onImageSelect,
//                onImageClicked = { selectedGalleryImage = it }
            )
//            AnimatedVisibility(visible = selectedGalleryImage != null) {
//                Dialog(onDismissRequest = { selectedGalleryImage = null }) {
//                    if (selectedGalleryImage != null) {
//                        ZoomableImage(
//                            selectedGalleryImage = selectedGalleryImage!!,
//                            onCloseClicked = { selectedGalleryImage = null },
//                            onDeleteClicked = {
//                                if (selectedGalleryImage != null) {
//                                    onImageDeleteClicked(selectedGalleryImage!!)
//                                    selectedGalleryImage = null
//                                }
//                            }
//                        )
//                    }
//                }
//            }
        },
    )
}

@Composable
@Preview
private fun WriteScreenContentPreview() {
    WriteScreenContent(
        events = {},
        state = state,
//        state = HomeState(),
    )
}
