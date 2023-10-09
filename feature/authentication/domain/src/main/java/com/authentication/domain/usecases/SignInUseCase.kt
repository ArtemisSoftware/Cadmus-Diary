package com.authentication.domain.usecases

import com.authentication.domain.repository.AuthenticationRepository
import com.core.domain.RequestState
import com.core.domain.repository.MongoRepository
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
