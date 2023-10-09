package com.authentication.presentation.auth

data class AuthenticationState(
    val isLoading: Boolean = false,
    val authenticated: Boolean = false,
)
