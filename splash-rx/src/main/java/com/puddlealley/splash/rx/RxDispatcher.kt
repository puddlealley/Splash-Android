package com.puddlealley.splash.rx

import com.jakewharton.rx.replayingShare
import com.jakewharton.rxrelay2.PublishRelay
import com.puddlealley.splash.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.toObservable



/**
 * Listen to updates from the dispatcher as an observable.
 */
fun Dispatcher.updates(): Observable<Payload> =
    Observable.create<Payload> { source ->

        val dispatchToken = register {
            source.onNext(it)
        }

        source.setDisposable(Disposables.fromAction {
            unregister(dispatchToken)
        })
    }.share()

fun FluxStore.changed(): Observable<Unit> =
    Observable.create<Unit> { source ->
        val removeListener = addListener { source.onNext(Unit) }
        source.setDisposable(Disposables.fromAction { removeListener() })
    }.share()

fun <T: State> FluxReduceStore<T>.updates() =
    changed().map { state }.distinctUntilChanged().replayingShare(state)

typealias Epic = (payloads: Observable<Payload>) -> Observable<Payload>

fun createEpicMiddleware(rootEpic: Epic): Middleware {
    return { next ->
        // create a relay to find all dispatched events
        val relay: PublishRelay<Payload> = PublishRelay.create()
        // connect all of the epics to the dispatcher
        rootEpic(relay).doOnNext { next(it) }.subscribe(next)
        // create a function forward the payload to the relay
        val payloadReceived = { action: Payload -> relay.accept(action) }
        // return that function so the dispatcher can call it
        payloadReceived
    }
}

/**
 * Merges all epics into a single epic.
 */
fun combineEpics(vararg epic: Epic): Epic = { payloads -> epic.toObservable().flatMap { it(payloads) } }
