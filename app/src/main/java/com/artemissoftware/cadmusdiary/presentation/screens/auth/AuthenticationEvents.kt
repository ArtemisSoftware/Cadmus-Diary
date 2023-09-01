package com.artemissoftware.cadmusdiary.presentation.screens.auth

sealed class AuthenticationEvents {

    data class SetLoading(val loading: Boolean) : AuthenticationEvents()

    data class SignInWithMongoAtlas(val tokenId: String) : AuthenticationEvents()
}
