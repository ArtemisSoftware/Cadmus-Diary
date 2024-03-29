package com.home.presentation.home.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.core.domain.models.JournalEntry
import com.home.presentation.R
import com.home.presentation.home.HomeState
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun HomeContent(
    paddingValues: PaddingValues,
    diaryNotes: Map<LocalDate, List<JournalEntry>>,
    onClick: (String) -> Unit,
    openGallery: (String) -> Unit,
    fetchImages: (String, List<String>) -> Unit,
    state: HomeState,
) {
    if (diaryNotes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .navigationBarsPadding()
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            diaryNotes.forEach { (localDate, diaries) ->
                stickyHeader(
                    key = localDate,
                    content = {
                        DateHeader(
                            localDate = localDate,
                            modifier = Modifier
                                .fillMaxWidth(),
                        )
                    },
                )

                items(
                    items = diaries,
                    key = { it.id },
                ) {
                    DiaryCard(
                        diary = it,
                        onClick = onClick,
                        openGallery = openGallery,
                        fetchImages = fetchImages,
                        galleryOpened = state.isGalleryOpen(it.id),
                        galleryLoading = state.isFetchingImages(it.id),
                        downloadedImages = state.downloadedImages(it.id),
                    )
                }
            }
        }
    } else {
        EmptyPage()
    }
}

@Composable
internal fun EmptyPage(
    @StringRes title: Int = R.string.empty_diary,
    subtitle: String = stringResource(id = R.string.write_something),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = title),
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.Medium,
            ),
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Normal,
            ),
        )
    }
}
