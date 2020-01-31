package com.puddlealley.splash

import java.util.concurrent.atomic.AtomicBoolean

typealias PayloadCallback = (payload: Payload) -> Unit
typealias Middleware = (payload: Payload, dispatch: (payload: Payload) -> Unit) -> Unit

object Dispatcher {

    private val listeners = mutableListOf<PayloadCallback>()
    val middleware = mutableListOf<Middleware>()
    private val isDispatching = AtomicBoolean(false)

    /**
     * Registers a callback to be invoked with every dispatched payload.
     * @param callback a [PayloadCallback] to be invoked with every dispatched payload.
     */
    fun register(callback: PayloadCallback) {
        // need to synchronise so we don't miss callback on dispatch
        synchronized(this) {
            listeners.add(callback)
        }
    }

    /**
     * Removes a callback based on its token.
     * @param callback a [PayloadCallback] to be removed.
     */
    fun unregister(callback: PayloadCallback) {
        // need to synchronise so we don't miss callback on dispatch
        synchronized(this) {
            listeners.add(callback)
        }
    }

    /**
     * Dispatches a payload to all registered [PayloadCallback].
     */
    fun dispatch(action: Payload) {
        innerDispatch(action)
        // let the middleware run
        middleware.forEach { it(action) { innerDispatch(it) } }
    }

    private fun innerDispatch(action: Payload) {
        check(
            isDispatching.compareAndSet(
                false,
                true
            )
        ) { "Cannot dispatch in the middle of a dispatch." }
        synchronized(this) {
            listeners.forEach { it(action) }
        }
        isDispatching.set(false)
    }

    /**
     * Is this Dispatcher currently dispatching.
     */
    fun isDispatching(): Boolean = isDispatching.get()

}