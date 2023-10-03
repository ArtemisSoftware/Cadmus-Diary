package com.artemissoftware.cadmusdiary.authentication.domain.repository

import com.google.firebase.auth.AuthCredential

interface AuthenticationRepository {

    suspend fun firebaseAuthentication(tokenId: String): Boolean
}
