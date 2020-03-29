package com.puddlealley.flux.store.login

import com.puddlealley.flux.service.UserDataFetcher
import com.puddlealley.flux.store.AppState
import com.puddlealley.splash.core.createEpic
import io.reactivex.rxkotlin.ofType

fun loginEpics(userDataFetcher: UserDataFetcher) =
    createEpic<AppState> { (events, state) ->
        val loginClicked = events.ofType<LoginEvents.LoginClicked>()
        // perform the login
        loginClicked.flatMap {
            val loginState = state.blockingFirst().loginState
            userDataFetcher.login(loginState.email, loginState.password)
        }
    }