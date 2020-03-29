package com.puddlealley.flux.store.login

import com.puddlealley.flux.service.LoginResult
import com.puddlealley.splash.core.Action
import com.puddlealley.splash.core.createReducer

val loginReducer =
    createReducer<LoginState> { result: Action ->
        copy(
            loading = reduceLoading(loading, result),
            email = reduceEmail(email, result),
            password = reducePassword(password, result)
        )
    }

fun reducePassword(password: String, result: Action): String {
    return when (result) {
        is LoginEvents.PasswordChanged -> result.password
        else -> password
    }
}

fun reduceEmail(email: String, result: Action): String =
    when (result) {
        is LoginEvents.EmailChanged -> result.email
        else -> email
    }

fun reduceLoading(loading: Boolean, result: Action): Boolean =
    when (result) {
        LoginResult.Loading -> true
        is LoginResult.Error -> false
        is LoginResult.Success -> false
        else -> loading
    }
