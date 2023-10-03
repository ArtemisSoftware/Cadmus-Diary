package com.artemissoftware.cadmusdiary.core.ui.components.modifier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.statusNavigationBarPadding(): Modifier =
    composed {
        background(MaterialTheme.colorScheme.surface) // important to maintain colors
            .statusBarsPadding() // important to maintain paddings
            .navigationBarsPadding() // important to maintain paddings
    }
