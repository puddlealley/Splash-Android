package com.puddlealley.splash.core

/**
 * A function that returns the next state tree, given
 * the current state tree and the action to handle.
 */
typealias Reducer<S> = (oldState: S, action: Action) -> S


fun <S : State> createReducer(short: S.(action: Action) -> S): Reducer<S> = { oldState, action -> short(oldState, action) }


fun <S : State> combineReducers(reducers: List<(oldState: S, action: Action) -> S>): (oldState: S, action: Action) -> S {
    return createReducer { action ->
        reducers.fold(this) { acc, function -> function(acc, action) }
    }
}

