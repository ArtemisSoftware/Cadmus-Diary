package com.artemissoftware.cadmusdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.artemissoftware.cadmusdiary.data.repository.MongoDB
import com.artemissoftware.cadmusdiary.navigation.NavGraph
import com.artemissoftware.cadmusdiary.ui.theme.CadmusDiaryTheme

class MainActivity : ComponentActivity() {

    private var keepSplashOpened = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        MongoDB.configureTheRealm()

        setContent {
            CadmusDiaryTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    onDataLoaded = {
                        keepSplashOpened = false
                    },
                )
            }
        }
    }
}
