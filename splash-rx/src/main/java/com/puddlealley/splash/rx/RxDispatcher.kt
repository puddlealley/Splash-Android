package com.puddlealley.splash.rx

import android.util.Log
import com.puddlealley.splash.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposables


class RxDispatcher(middleware: Middleware= {{}}) : Dispatcher(middleware) {
    /**
     * Listen to updates from the dispatcher as an observable.
     */
    val updates : Observable<Payload> =
        Observable.create<Payload> { source ->
            val dispatchToken = register {
                source.onNext(it)
            }

            source.setDisposable(Disposables.fromAction {
                unregister(dispatchToken)
            })
        }.share()
}

