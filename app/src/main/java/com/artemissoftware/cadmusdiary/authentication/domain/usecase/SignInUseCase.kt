package com.artemissoftware.cadmusdiary.authentication.domain.usecase

import com.artemissoftware.cadmusdiary.authentication.domain.repository.AuthenticationRepository
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import com.artemissoftware.cadmusdiary.util.Constants.APP_ID
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authenticationRepository: AuthenticationRepository) {

    suspend operator fun invoke(tokenId: String) = withContext(Dispatchers.IO) {
        try {
            authenticationRepository.firebaseAuthentication(tokenId)

            val result = App.create(APP_ID).login(
                Credentials.jwt(tokenId),
                // Credentials.google(tokenId, GoogleAuthType.ID_TOKEN), // needs fix from google to be able to see name, email, picture.....
            ).loggedIn

            RequestState.Success(result)
        } catch (ex: Exception) {
            RequestState.Error(ex)
        }
    }
}
