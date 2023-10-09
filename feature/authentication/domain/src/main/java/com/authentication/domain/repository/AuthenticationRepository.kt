package com.authentication.domain.repository

interface AuthenticationRepository {

    suspend fun firebaseAuthentication(tokenId: String): Boolean
}
