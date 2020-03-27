package com.puddlealley.flux.store.device

import com.puddlealley.flux.service.CodeVerificationResult
import com.puddlealley.splash.core.Action
import com.puddlealley.splash.core.createReducer
import timber.log.Timber

val secretCaveReducer = createReducer<SecretCaveState> { action ->
    copy(
        loading = reducerLoading(loading, action),
        codeCorrect = reduceCodeCorrect(codeCorrect, action),
        enteredCode = reduceEnteredCode(enteredCode, action)
    )
}

fun reduceEnteredCode(enteredCode: String, action: Action): String =
    when (action) {
        else -> enteredCode
    }

fun reducerLoading(loading: Boolean, action: Action): Boolean =
    when (action) {
        else -> loading
    }

fun reduceCodeCorrect(codeCorrect: Boolean, action: Action): Boolean =
    when (action) {
        else -> codeCorrect
    }