package com.rockspin.flux.store.login

import com.puddlealley.splash.core.*

sealed class LoginEvents : Event {
    data class EmailChanged(val email: String) : LoginEvents()
    data class PasswordChanged(val password: String) : LoginEvents()
    data class LoginClicked(val email: String, val password: String) : LoginEvents()
}

data class LoginState(
    // Loading state.
    val loading: Boolean = false,
    // Form state.
    val email: String = "",
    val password: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    // Button state.
    val canSignIn: Boolean = false
) : State