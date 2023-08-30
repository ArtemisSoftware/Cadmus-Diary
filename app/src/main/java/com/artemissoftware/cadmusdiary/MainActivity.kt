package com.artemissoftware.cadmusdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.artemissoftware.cadmusdiary.ui.theme.CadmusDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CadmusDiaryTheme {
            }
        }
    }
}
