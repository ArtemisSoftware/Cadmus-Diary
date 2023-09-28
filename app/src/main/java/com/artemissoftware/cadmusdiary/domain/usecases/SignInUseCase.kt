package com.artemissoftware.cadmusdiary.domain.usecases

import com.artemissoftware.cadmusdiary.domain.RequestState
import com.artemissoftware.cadmusdiary.domain.repository.AuthenticationRepository
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import com.google.firebase.auth.GoogleAuthProvider
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    suspend operator fun invoke(tokenId: String) = withContext(Dispatchers.IO) {
        val credential = GoogleAuthProvider.getCredential(tokenId, null)

        try {
            authenticationRepository.firebaseAuthentication(credential)

            val result = App.create(APP_ID).login(
                Credentials.jwt(tokenId),
                //            Credentials.google(tokenId, GoogleAuthType.ID_TOKEN), // needs fix from google to be able to see name, email, picture.....
            ).loggedIn

            RequestState.Success(result)
        } catch (ex: Exception) {
            RequestState.Error(ex)
        }
    }
}
