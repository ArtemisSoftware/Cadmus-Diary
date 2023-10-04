package com.artemissoftware.cadmusdiary.authentication.domain.usecase

import com.artemissoftware.cadmusdiary.authentication.domain.repository.AuthenticationRepository
import com.artemissoftware.cadmusdiary.core.data.repository.MongoRepository
import com.artemissoftware.cadmusdiary.core.domain.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    private val mongoRepository: MongoRepository,
) {

    suspend operator fun invoke(tokenId: String) = withContext(Dispatchers.IO) {
        try {
            authenticationRepository.firebaseAuthentication(tokenId)
            val result = mongoRepository.login(tokenId)

            RequestState.Success(result)
        } catch (ex: Exception) {
            RequestState.Error(ex)
        }
    }
}
