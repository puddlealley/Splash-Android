package com.puddlealley.flux.store.device

import com.puddlealley.splash.core.Event
import com.puddlealley.splash.core.State


data class SecretCaveState(
    val loading : Boolean = false,
    val codeCorrect : Boolean = false,
    val enteredCode: String = ""
): State

sealed class SecretCaveEvents : Event {
    data class LetteredEntered(val letter: String) : SecretCaveEvents()
    data class CodeEntered(val code: String) : SecretCaveEvents()
}

