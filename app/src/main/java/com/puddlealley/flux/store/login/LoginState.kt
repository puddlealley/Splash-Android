package com.puddlealley.flux.store.login

import com.puddlealley.flux.validate.EmailValidResult
import com.puddlealley.flux.validate.PasswordValidResult
import com.puddlealley.flux.validate.isEmailValid
import com.puddlealley.flux.validate.isValidPassword
import com.puddlealley.splash.core.Event
import com.puddlealley.splash.core.State

sealed class LoginEvents : Event {
    data class EmailChanged(val email: String) : LoginEvents()
    data class PasswordChanged(val password: String) : LoginEvents()
    object LoginClicked : LoginEvents()
}

data class LoginState(
    // Loading state.
    val loading: Boolean = false,
    // Form state.
    val email: String = "",
    val password: String = ""
) : State {

    val emailError: String =
        when (email.isEmailValid()) {
            is EmailValidResult.Valid -> ""
            is EmailValidResult.TooShort -> "Email Too short :("
            is EmailValidResult.BadlyFormatted -> "Email badly formatted :("
        }

    val passwordError: String =
        when (password.isValidPassword()) {
            PasswordValidResult.Valid -> ""
            PasswordValidResult.TooShort -> "Password too short :("
        }

    // Button state.
    val canSignIn: Boolean = emailError.isEmpty() && passwordError.isEmpty()
}