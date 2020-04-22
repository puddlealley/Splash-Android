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
    ).apply {
        timestamp = System.currentTimeMillis()
    }
}

fun reduceEnteredCode(enteredCode: String, action: Action): String =
    when (action) {
        is SecretCaveEvents.LetteredEntered -> if(enteredCode.length >= 7) {
            action.letter
        } else {
            enteredCode + action.letter
        }
        is SecretCaveEvents.CodeEntered -> action.code
        else -> enteredCode
    }

fun reducerLoading(loading: Boolean, action: Action): Boolean =
    when (action) {
        is CodeVerificationResult ->
            when (action) {
                is CodeVerificationResult.Loading -> true
                is CodeVerificationResult.Error -> false
                is CodeVerificationResult.Success -> false
            }
        else -> loading
    }

fun reduceCodeCorrect(codeCorrect: Boolean, action: Action): Boolean =
    when (action) {
        is CodeVerificationResult ->
            when (action) {
                is CodeVerificationResult.Success -> action.correct
                CodeVerificationResult.Loading -> false
                is CodeVerificationResult.Error -> false
            }
        else -> codeCorrect
    }