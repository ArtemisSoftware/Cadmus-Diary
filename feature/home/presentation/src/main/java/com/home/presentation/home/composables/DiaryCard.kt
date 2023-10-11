package com.home.presentation.home.composables

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.artemissoftware.util.extensions.toInstant
import com.core.domain.models.Mood
import com.core.ui.models.toMoodUi
import com.core.ui.theme.Elevation
import com.artemissoftware.util.DateTimeConstants
import com.core.domain.models.JournalEntry
import com.home.presentation.R
import io.realm.kotlin.ext.realmListOf
import org.mongodb.kbson.ObjectId
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DiaryCard(
    diary: JournalEntry,
    onClick: (String) -> Unit,
    openGallery: (String) -> Unit,
    fetchImages: (String, List<String>) -> Unit,
    galleryOpened: Boolean,
    galleryLoading: Boolean,
    downloadedImages: List<Uri> = emptyList(),
) {
    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }

    LaunchedEffect(key1 = galleryOpened) {
        if (galleryOpened && downloadedImages.isEmpty()) {
            fetchImages(diary.id, diary.images)
        }
    }

    Row(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = {
//                    onClick(diary._id.toHexString())
                },
            )
    ) {
        Spacer(modifier = Modifier.width(14.dp))

        TimeLine(height = componentHeight)

        Spacer(modifier = Modifier.width(20.dp))

        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) { it.size.height.toDp() }
                },
            tonalElevation = Elevation.Level1,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(moodName = diary.mood, time = diary.date)
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = diary.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                )

                if (diary.images.isNotEmpty()) {
                    ShowGalleryButton(
                        galleryOpened = galleryOpened,
                        galleryLoading = galleryLoading,
                        onClick = {
                            openGallery.invoke(diary.id)
                        },
                    )
                }
                AnimatedVisibility(
                    visible = galleryOpened && !galleryLoading,
                    enter = fadeIn() + expandVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow,
                        ),
                    ),
                ) {
                    Column(modifier = Modifier.padding(all = 16.dp)) {
                        Gallery(images = downloadedImages, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

@Composable
private fun DiaryHeader(moodName: String, time: Instant) {
    val mood by remember { mutableStateOf(moodName.toMoodUi()) }
    val formatter = remember {
        DateTimeFormatter.ofPattern(DateTimeConstants.FORMAT_hh_mm_a, Locale.getDefault())
            .withZone(ZoneId.systemDefault())
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = "Mood Icon",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
            )
        }
        Text(
            text = formatter.format(time),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize),
        )
    }
}

@Composable
@Preview
private fun DiaryHeaderPreview() {
    DiaryHeader(
        moodName = Mood.Happy.name,
        time = Instant.EPOCH,
    )
}

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
    galleryLoading: Boolean,
    onClick: () -> Unit,
) {
    TextButton(onClick = onClick) {
        Text(
            text = stringResource(
                id = if (galleryOpened) {
                    if (galleryLoading) R.string.loading else R.string.hide_loading
                } else {
                    R.string.show_loading
                },
            ),
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize),
        )
    }
}

@Composable
@Preview
private fun DiaryCardPreview() {
    DiaryCard(
        diary = JournalEntry(
            id = "2",
            title = "My Diary",
            ownerId = "23",
            date = Instant.now(),
            description =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        ),
        onClick = {},
        openGallery = {},
        fetchImages = { _, _ -> },
        galleryLoading = true,
        galleryOpened = true,
    )
}

@Composable
@Preview
private fun DiaryCard_no_images_Preview() {
    DiaryCard(
        diary = JournalEntry(
            id = "2",
            title = "My Diary",
            ownerId = "23",
            date = Instant.now(),
            description =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit ex ea commodo consequat.",
        ),
        onClick = {},
        openGallery = {},
        fetchImages = { _, _ -> },
        galleryLoading = true,
        galleryOpened = true,
    )
}
