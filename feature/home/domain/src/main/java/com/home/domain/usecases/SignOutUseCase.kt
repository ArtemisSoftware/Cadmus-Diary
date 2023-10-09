package com.home.domain.usecases

import com.core.domain.repository.MongoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val mongoRepository: MongoRepository) {

    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        mongoRepository.logout()
    }
}
