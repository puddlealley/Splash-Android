package com.puddlealley.splash.rx

import com.jakewharton.rxrelay2.PublishRelay
import com.puddlealley.splash.Middleware
import com.puddlealley.splash.Payload
import io.reactivex.Observable
import io.reactivex.rxkotlin.mergeAll
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.toObservable


typealias Epic = (payloads: Observable<Payload>) -> Observable<Payload>

class EpicMiddleware(private val rootEpic: Epic) : Middleware {
    // create a relay to find all dispatched events
    private val relay: PublishRelay<Payload> = PublishRelay.create()
    private val epic: PublishRelay<Epic> = PublishRelay.create()

    override fun invoke(dispatcher: (payload: Payload) -> Unit): (payload: Payload) -> Unit {
        // connect all of the epics to the dispatcher
        epic.map { it(relay) }.mergeAll().subscribe(dispatcher)
        // create a function forward the payload to the relay
        return { action: Payload -> relay.accept(action) }
    }

    fun run() = epic.accept(rootEpic)
}

fun createEpicMiddleware(rootEpic: Epic): EpicMiddleware = EpicMiddleware(rootEpic)

/**
 * Merges all epics into a single epic.
 */
fun combineEpics(vararg epic: Epic): Epic =
    { payloads -> epic.toObservable().flatMap { it(payloads) } }

fun epic(create: Epic) = create

inline fun <reified T : Payload> epicOfType(
    crossinline create: (Observable<T>) -> Observable<Payload>
) = epic { create(it.ofType()) }

