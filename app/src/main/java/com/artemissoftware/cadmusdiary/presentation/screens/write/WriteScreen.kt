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
//    pagerState: PagerState,
//    galleryState: GalleryState,
//    moodName: () -> String,
//    onTitleChanged: (String) -> Unit,
//    onDescriptionChanged: (String) -> Unit,
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
        events = viewModel::onTriggerEvent,
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
                state = state,
                onDeleteConfirmed = {
                    events.invoke(WriteEvents.DeleteDiary)
                },
                onBackPressed = { events.invoke(WriteEvents.PopBackStack) },
                onDateTimeUpdated = {
                    events.invoke(WriteEvents.UpdateDateTime(zonedDateTime = it))
                },
            )
        },
        content = { paddingValues ->
            WriteContent(
                state = state,
//                pagerState = pagerState,
//                galleryState = galleryState,
                onTitleChanged = {
                    events.invoke(WriteEvents.SetTitle(title = it))
                },
                onDescriptionChanged = {
                    events.invoke(WriteEvents.SetDescription(description = it))
                },
                onMoodScroll = {
                    events.invoke(WriteEvents.SetMood(mood = it))
                },
                onSaveClicked = {
                    events.invoke(WriteEvents.SaveDiary)
                },
                addImage = { imageUri, path ->
                    events.invoke(WriteEvents.AddImage(imageUri, path))
                },
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
        state = WriteState(),
//        state = HomeState(),
    )
}
