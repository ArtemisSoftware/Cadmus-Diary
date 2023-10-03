package com.artemissoftware.cadmusdiary.core.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.artemissoftware.cadmusdiary.ui.theme.Elevation

@ExperimentalMaterial3Api
@Composable
fun AddImageButton(
    imageSize: Dp,
    imageShape: CornerBasedShape,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .size(imageSize)
            .clip(shape = imageShape),
        onClick = onClick,
        tonalElevation = Elevation.Level1,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Icon",
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun AddImageButtonPreview() {
    AddImageButton(
        imageSize = 40.dp,
        imageShape = Shapes().small,
        onClick = {},
    )
}
