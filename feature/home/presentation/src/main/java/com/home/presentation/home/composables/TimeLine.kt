package com.home.presentation.home.composables

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.core.ui.theme.Elevation

@Composable
internal fun TimeLine(height: Dp) {
    Surface(
        modifier = Modifier
            .width(2.dp)
            .height(height + 14.dp),
        tonalElevation = Elevation.Level1,
        content = {},
    )
}

@Composable
@Preview
private fun TimeLinePreview() {
    TimeLine(
        height = 24.dp,
    )
}
