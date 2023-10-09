package com.authentication.data.repository

import com.authentication.domain.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthenticationRepositoryImpl : AuthenticationRepository {
    override suspend fun firebaseAuthentication(tokenId: String): Boolean {
        val credential = GoogleAuthProvider.getCredential(tokenId, null)

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
