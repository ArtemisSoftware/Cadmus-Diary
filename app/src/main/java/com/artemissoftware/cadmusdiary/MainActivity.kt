package com.artemissoftware.cadmusdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.artemissoftware.cadmusdiary.navigation.NavGraph
import com.artemissoftware.cadmusdiary.navigation.Screen
import com.artemissoftware.cadmusdiary.ui.theme.CadmusDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            CadmusDiaryTheme {
                val navController = rememberNavController()
                NavGraph(startDestination = Screen.Authentication.route, navController = navController)
            }
        }
    }
}
