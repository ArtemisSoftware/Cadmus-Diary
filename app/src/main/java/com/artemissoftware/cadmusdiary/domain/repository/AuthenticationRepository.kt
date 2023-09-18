package com.artemissoftware.cadmusdiary.domain.repository

import com.google.firebase.auth.AuthCredential

interface AuthenticationRepository {

    suspend fun firebaseAuthentication(credential: AuthCredential): Boolean
}
