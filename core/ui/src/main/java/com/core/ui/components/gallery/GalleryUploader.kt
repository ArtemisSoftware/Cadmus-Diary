package com.core.ui.components.gallery

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.core.ui.components.PlusValueIcon
import com.core.ui.components.buttons.AddImageButton
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryUploader(
    modifier: Modifier = Modifier,
    images: List<Uri>,
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = Shapes().medium,
    spaceBetween: Dp = 12.dp,
    onAddClicked: () -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (Int) -> Unit,
) {
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 8),
        onResult = { imagesResult ->
            imagesResult.forEach {
                onImageSelect(it)
            }
        },
    )

    BoxWithConstraints(modifier = modifier) {
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(
                    a = 0,
                    b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(2),
                )
            }
        }

//        val remainingImages = remember {
//            derivedStateOf {
//                images.size - numberOfVisibleImages.value
//            }
//        }

        val remainingImages = images.size - numberOfVisibleImages.value

        Row {
            AddImageButton(
                imageSize = imageSize,
                imageShape = imageShape,
                onClick = {
                    onAddClicked()
                    multiplePhotoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )
            Spacer(modifier = Modifier.width(spaceBetween))
            images.take(numberOfVisibleImages.value).forEachIndexed { index, imageUri ->
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize)
                        .clickable { onImageClicked(index) },
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUri)
                        .crossfade(true)
                        .build(),
                    contentScale = ContentScale.Crop,
                    contentDescription = "Gallery Image",
                )
                Spacer(modifier = Modifier.width(spaceBetween))
            }
            if (remainingImages/*.value*/ > 0) {
                PlusValueIcon(
                    imageSize = imageSize,
                    imageShape = imageShape,
                    plusValue = remainingImages/*.value*/,
                )
            }
        }
    }
}

@Composable
@Preview
private fun GalleryUploaderPreview() {
    GalleryUploader(
        images = listOf(Uri.EMPTY, Uri.EMPTY),
        onAddClicked = {},
        onImageSelect = {},
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Lolo(
    modifier: Modifier = Modifier,
    imagesL: List<Uri>,
    imageSize: Dp = 60.dp,
    imageShape: CornerBasedShape = Shapes().medium,
    spaceBetween: Dp = 12.dp,
    onAddClicked: () -> Unit,
    onImageSelect: (Uri) -> Unit,
    onImageClicked: (Int) -> Unit,
) {
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 8),
        onResult = { imagesResult ->
            imagesResult.forEach {
                onImageSelect(it)
            }
        },
    )

    val images = imagesL.map { it.toString().get(0).toString() }

    BoxWithConstraints(modifier = modifier) {
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(
                    a = 0,
                    b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(2),
                )
            }
        }

//        val remainingImages = remember {
//            derivedStateOf {
//                images.size - numberOfVisibleImages.value
//            }
//        }

        val remainingImages = images.size - numberOfVisibleImages.value

        Row {
            AddImageButton(
                imageSize = imageSize,
                imageShape = imageShape,
                onClick = {
                    onAddClicked()
                    multiplePhotoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )
            Spacer(modifier = Modifier.width(spaceBetween))
            images.take(numberOfVisibleImages.value).forEachIndexed { index, imageUri ->

                Box(
                    modifier = Modifier
                        .background(color = Color.Yellow)
                        .clip(imageShape)
                        .size(imageSize),
                ) {
                    Text(text = imageUri)
                }

                Spacer(modifier = Modifier.width(spaceBetween))
            }
            if (remainingImages/*.value*/ > 0) {
                PlusValueIcon(
                    imageSize = imageSize,
                    imageShape = imageShape,
                    plusValue = remainingImages/*.value*/,
                )
            }
        }
    }
}

@Composable
@Preview
private fun GalleryUploader_Preview() {
//    Lolo(
//        images = listOf("A","B","C","D","E","F"),
//        onAddClicked = {},
//        onImageSelect = {},
//    ) {}
}
