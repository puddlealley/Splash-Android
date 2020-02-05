package com.puddlealley.splash.rx

import com.jakewharton.rx.replayingShare
import com.puddlealley.splash.Dispatcher
import com.puddlealley.splash.FluxReduceStore
import com.puddlealley.splash.State
import io.reactivex.Observable
import io.reactivex.disposables.Disposables

abstract class RxFluxReduceStore<T : State>(
    initialState: T,
    dispatcher: Dispatcher
) : FluxReduceStore<T>(initialState, dispatcher) {

    val onChanged: Observable<Unit> =
        Observable.create<Unit> { source ->
            val removeListener = addListener { source.onNext(Unit) }
            source.setDisposable(Disposables.fromAction {
                removeListener()
            })
        }.share()

    val updates: Observable<T> = onChanged
        .map { state }
        .startWith(Observable.defer { Observable.just(state) })
        .replayingShare()
        .distinctUntilChanged()


}