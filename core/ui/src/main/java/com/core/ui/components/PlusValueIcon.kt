package com.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.ui.R

@Composable
fun PlusValueIcon(
    imageSize: Dp,
    imageShape: CornerBasedShape,
    plusValue: Int,
) {
    Box(contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .clip(imageShape)
                .size(imageSize),
            color = MaterialTheme.colorScheme.primaryContainer,
            content = {},
        )
        Text(
            text = stringResource(R.string.add_with_value, plusValue),
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Medium,
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
@Preview
private fun PlusValueIcon() {
    PlusValueIcon(
        imageSize = 40.dp,
        imageShape = Shapes().small,
        plusValue = 3,
    )
}
