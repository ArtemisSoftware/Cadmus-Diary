package com.artemissoftware.cadmusdiary.home.presentation.home.composables

import android.net.Uri
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.artemissoftware.cadmusdiary.core.ui.components.PlusValueIcon
import kotlin.math.max

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<Uri>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 12.dp,
    imageShape: CornerBasedShape = Shapes().small,
) {
    BoxWithConstraints(modifier = modifier) {
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(
                    a = 0,
                    b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(1),
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
            images.take(numberOfVisibleImages.value).forEach { image ->
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
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
private fun GalleryPreview() {
    Gallery(
        modifier = Modifier.fillMaxWidth(),
        images = listOf(Uri.EMPTY, Uri.EMPTY),
    )
}
