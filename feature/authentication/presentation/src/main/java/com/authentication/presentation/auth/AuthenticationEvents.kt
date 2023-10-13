package com.authentication.presentation.auth

internal sealed class AuthenticationEvents {

    data class SetLoading(val loading: Boolean) : AuthenticationEvents()

    data class SignIn(val tokenId: String) : AuthenticationEvents()
}
