package com.rockspin.flux.store.login

import com.puddlealley.splash.core.Action
import com.puddlealley.splash.core.createReducer
import com.rockspin.flux.service.LoginResult
import com.rockspin.flux.validate.EmailValidResult
import com.rockspin.flux.validate.PasswordValidResult

val loginReducer =
    createReducer<LoginState> { result: Action ->
        when (result) {
            is LoginResult -> {
                when (result) {
                    LoginResult.Loading -> copy(loading = true)
                    is LoginResult.Error -> copy(
                        loading = false
                    )
                    is LoginResult.Success -> copy(
                        loading = false
                    )
                }
            }
            is EmailValidResult -> {
                when (result) {
                    EmailValidResult.Valid ->
                        copy(emailError = "")
                    EmailValidResult.TooShort ->
                        copy(
                            emailError = "Email Too short :("
                        )
                    EmailValidResult.BadlyFormatted ->
                        copy(
                            emailError = "Email badly formatted Short :("
                        )
                }
            }
            is PasswordValidResult -> {
                when (result) {
                    PasswordValidResult.Valid -> copy(
                        passwordError = ""
                    )
                    PasswordValidResult.TooShort -> copy(
                        passwordError = "Password too short :("
                    )
                }
            }
            is LoginEvents.EmailChanged -> copy(email = result.email)
            is LoginEvents.PasswordChanged -> copy(
                password = result.password
            )
            is CanLogin -> copy(canSignIn = result.canLogin)
            else -> this
        }
    }