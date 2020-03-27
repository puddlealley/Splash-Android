package com.rockspin.flux.store.login

import com.puddlealley.splash.core.Result
import com.puddlealley.splash.core.createEpic
import com.rockspin.flux.service.UserDataFetcher
import com.rockspin.flux.store.AppState
import com.rockspin.flux.validate.EmailValidResult
import com.rockspin.flux.validate.PasswordValidResult
import com.rockspin.flux.validate.isEmailValid
import com.rockspin.flux.validate.isValidPassword
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.merge
import io.reactivex.rxkotlin.ofType

data class CanLogin(val canLogin: Boolean) : Result

fun loginEpics(userDataFetcher: UserDataFetcher) =
    createEpic<AppState> { (events) ->
        // get the events we care about
        val passwordChanged =
            events.ofType<LoginEvents.PasswordChanged>()
        val emailChanged =
            events.ofType<LoginEvents.EmailChanged>()
        val loginClicked =
            events.ofType<LoginEvents.LoginClicked>()

        // check that form data is valid
        val isPasswordValid = passwordChanged.map { it.password.isValidPassword() }.share()
        val isEmailValid = emailChanged.map { it.email.isEmailValid() }.share()

        // check if the login button should be enabled
        val canLogin = Observables.combineLatest(
            isPasswordValid,
            isEmailValid
        ) { passwordValid: PasswordValidResult, emailValid ->
            passwordValid is PasswordValidResult.Valid && emailValid is EmailValidResult.Valid
        }.map { CanLogin(it) }

        // perform the login
        val loginUser = loginClicked.flatMap { userDataFetcher.login(it.email, it.password) }

        listOf(
            isPasswordValid,
            isEmailValid,
            canLogin,
            loginUser
        ).merge()
    }