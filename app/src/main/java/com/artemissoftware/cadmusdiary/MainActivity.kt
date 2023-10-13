package com.artemissoftware.cadmusdiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.core.ui.theme.CadmusDiaryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            viewModel.state.value.keepSplashOpened
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            CadmusDiaryTheme {
                MainScreen(viewModel = viewModel)
            }
        }

        // TODO: meter isto num work manager
//        cleanupCheck(
//            scope = lifecycleScope,
//            imageToUploadDao = imageToUploadDao,
// //            imageToDeleteDao = imageToDeleteDao
//        )
    }

//    private fun cleanupCheck(
//        scope: CoroutineScope,
//        imageToUploadDao: ImageToUploadDao,
// //        imageToDeleteDao: ImageToDeleteDao
//    ) {
//        scope.launch(Dispatchers.IO) {
//            val result = imageToUploadDao.getAllImages()
//            result.forEach { imageToUpload ->
//                retryUploadingImageToFirebase(
//                    imageToUpload = imageToUpload,
//                    onSuccess = {
//                        scope.launch(Dispatchers.IO) {
//                            imageToUploadDao.cleanupImage(imageId = imageToUpload.id)
//                        }
//                    },
//                )
//            }
// //            val result2 = imageToDeleteDao.getAllImages()
// //            result2.forEach { imageToDelete ->
// //                retryDeletingImageFromFirebase(
// //                    imageToDelete = imageToDelete,
// //                    onSuccess = {
// //                        scope.launch(Dispatchers.IO) {
// //                            imageToDeleteDao.cleanupImage(imageId = imageToDelete.id)
// //                        }
// //                    }
// //                )
// //            }
//        }
//    }
}
//
// fun retryUploadingImageToFirebase(
//    imageToUpload: ImageToUploadEntity,
//    onSuccess: () -> Unit,
// ) {
//    val storage = FirebaseStorage.getInstance().reference
//    storage.child(imageToUpload.remoteImagePath).putFile(
//        imageToUpload.imageUri.toUri(),
//        storageMetadata { },
//        imageToUpload.sessionUri.toUri(),
//    ).addOnSuccessListener { onSuccess() }
// }
