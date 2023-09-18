package com.artemissoftware.cadmusdiary.data.repository

import com.artemissoftware.cadmusdiary.domain.repository.AuthenticationRepository
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override suspend fun firebaseAuthentication(credential: AuthCredential): Boolean {
        val firebaseAuth = FirebaseAuth.getInstance()

        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(true)
                    } else {
                        task.exception?.let {
                            continuation.resumeWithException(it)
                        }
                    }
                }
        }
    }
}
