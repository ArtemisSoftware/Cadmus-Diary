package com.artemissoftware.cadmusdiary.home.domain.usecases

import com.artemissoftware.cadmusdiary.util.Constants
import io.realm.kotlin.mongodb.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase @Inject constructor() {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val user = App.create(Constants.APP_ID).currentUser
        if (user != null) {
            user.logOut()
            true
        } else {
            false
        }
    }
}
