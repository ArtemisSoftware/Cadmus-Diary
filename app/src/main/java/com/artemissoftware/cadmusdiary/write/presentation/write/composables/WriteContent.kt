package com.artemissoftware.cadmusdiary.write.presentation.write.composables

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.artemissoftware.cadmusdiary.R
import com.artemissoftware.cadmusdiary.core.ui.components.gallery.GalleryUploader
import com.artemissoftware.cadmusdiary.core.ui.mood.MoodUI
import com.artemissoftware.cadmusdiary.write.presentation.write.WriteState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WriteContent(
    state: WriteState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    paddingValues: PaddingValues,
    onMoodScroll: (MoodUI) -> Unit,
    onSaveClicked: () -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (Int) -> Unit,
) {
    val pagerState = rememberPagerState { MoodUI.values().size }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    LaunchedEffect(key1 = state.mood) {
        pagerState.scrollToPage(MoodUI.valueOf(state.mood.name).ordinal)
    }

    LaunchedEffect(key1 = pagerState.currentPage) {
        val pageMood = MoodUI.values()[pagerState.currentPage]
        if (pageMood != state.mood) {
            onMoodScroll.invoke(pageMood)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(state = scrollState),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth(),
                state = pagerState,
            ) { page ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        modifier = Modifier.size(120.dp),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(MoodUI.values()[page].icon)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Mood Image",
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = onTitleChanged,
                placeholder = { Text(text = stringResource(R.string.title)) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        scope.launch {
                            scrollState.animateScrollTo(Int.MAX_VALUE)
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                ),
                maxLines = 1,
                singleLine = true,
            )
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.description,
                onValueChange = onDescriptionChanged,
                placeholder = { Text(text = "Tell me about it.") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    },
                ),
            )
        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            GalleryUploader(
                modifier = Modifier.fillMaxWidth(),
                images = state.getImagesUri(),
                onAddClicked = { focusManager.clearFocus() },
                onImageSelect = onImageSelect,
                onImageClicked = onImageClicked,
            )
//            Lolo(
//                modifier = Modifier.fillMaxWidth(),
//                imagesL = state.getImagesUri(),
//                onAddClicked = { focusManager.clearFocus() },
//                onImageSelect = onImageSelect,
//                onImageClicked = onImageClicked,
//            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                onClick = onSaveClicked,
                shape = Shapes().small,
            ) {
                Text(text = stringResource(R.string.save))
            }
        }
    }
}

@Composable
@Preview
private fun WriteContentPreview() {
    WriteContent(
        state = WriteState(
            title = "Title one",
            description = "Description one",
        ),
        onTitleChanged = {},
        onDescriptionChanged = {},
        paddingValues = PaddingValues(16.dp),
        onMoodScroll = {},
        onSaveClicked = {},
        onImageSelect = {},
        onImageClicked = {},
    )
}
