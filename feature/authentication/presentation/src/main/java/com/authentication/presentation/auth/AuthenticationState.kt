package com.authentication.presentation.auth

internal data class AuthenticationState(
    val isLoading: Boolean = false,
    val authenticated: Boolean = false,
)
