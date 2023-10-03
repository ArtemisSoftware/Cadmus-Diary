package com.artemissoftware.cadmusdiary.authentication.presentation.auth

data class AuthenticationState(
    val isLoading: Boolean = false,
    val authenticated: Boolean = false,
)
