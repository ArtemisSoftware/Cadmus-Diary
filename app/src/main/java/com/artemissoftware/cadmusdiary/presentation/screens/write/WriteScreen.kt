package com.artemissoftware.cadmusdiary.presentation.screens.write

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.artemissoftware.cadmusdiary.presentation.components.events.UIEventsManager
import com.artemissoftware.cadmusdiary.presentation.components.image.ZoomableImage
import com.artemissoftware.cadmusdiary.presentation.screens.write.composables.WriteContent
import com.artemissoftware.cadmusdiary.presentation.screens.write.composables.WriteTopBar

@Composable
fun WriteScreen(
    viewModel: WriteViewModel = hiltViewModel(),
    navController: NavHostController,
//    pagerState: PagerState,
//    galleryState: GalleryState,
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
//                onImageSelect = onImageSelect,
                onImageClicked = {
                    events.invoke(WriteEvents.ZoomInImage(image = it))
                }
            )

            AnimatedVisibility(visible = state.selectedImage != null) {
                Dialog(
                    onDismissRequest = {
                        events.invoke(WriteEvents.ZoomOutImage)
                    },
                    content = {
                        state.selectedImage?.let { selectedImage ->
                            ZoomableImage(
                                selectedGalleryImage = selectedImage,
                                onCloseClicked = {
                                    events.invoke(WriteEvents.ZoomOutImage)
                                },
                                onDeleteClicked = {

//                                    onImageDeleteClicked(selectedImage)
                                    events.invoke(WriteEvents.ZoomOutImage)

                                }
                            )
                        }
                    }
                )
            }
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
