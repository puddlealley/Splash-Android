package com.puddlealley.flux.store.device

import com.puddlealley.flux.service.ApiRequests
import com.puddlealley.flux.store.AppState
import com.puddlealley.splash.core.createEpic
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType

fun secretCarvEpic(apiRequests: ApiRequests) =
    createEpic<AppState> { (actions) ->
        val codeEntered = actions.ofType<SecretCaveEvents.CodeEntered>()
        codeEntered
            .map { it.code }
            .switchMap { apiRequests.codeVerification(it) }
    }