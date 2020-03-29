package com.puddlealley.flux.store

import com.puddlealley.splash.core.DiStore
import com.puddlealley.splash.core.State
import com.puddlealley.splash.core.createReducer
import com.puddlealley.splash.core.toMiddleware
import com.puddlealley.flux.service.UserDataFetcher
import com.puddlealley.flux.store.login.LoginState
import com.puddlealley.flux.store.login.loginEpics
import com.puddlealley.flux.store.login.loginReducer

/**
 * A store that holds and updates the application state.
 */
class AppStore(userDataFetcher: UserDataFetcher) : DiStore<AppState>(
    appReducer,
    loginEpics(userDataFetcher).toMiddleware(),
    AppState()
)

/**
 * The state that represents all data in the application.
 */
data class AppState(
    val loginState: LoginState = LoginState()
) : State

/**
 * An app reducer updates the app state based on dispatched actions.
 */
val appReducer = createReducer<AppState> { action ->
    copy(
        loginState = loginReducer(loginState, action)
    )
}