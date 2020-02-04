package com.puddlealley.splash

import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

typealias PayloadCallback = (payload: Payload) -> Unit
typealias Middleware = (payload: Payload, dispatch: (payload: Payload) -> Unit) -> Unit
typealias DispatchToken = UUID

class Dispatcher {

    private val callbacks = mutableMapOf<DispatchToken, PayloadCallback>()
    private val middleware = mutableListOf<Middleware>()
    private val isDispatching = AtomicBoolean(false)

    // dispatch state
    private val isPending = mutableMapOf<DispatchToken, Boolean>()
    private val isHandled = mutableMapOf<DispatchToken, Boolean>()
    private var pendingPayload: Payload? = null

    /**
     * Registers a callback to be invoked with every dispatched payload.
     * @param callback a [PayloadCallback] to be invoked with every dispatched payload.
     */
    fun register(callback: PayloadCallback): DispatchToken {
        val randomUUID = UUID.randomUUID()
        callbacks[randomUUID] = callback
        return randomUUID
    }

    /**
     * Removes a callback based on its token.
     * @param callback a [PayloadCallback] to be removed.
     */
    fun unregister(dispatchToken: DispatchToken) {
        check(callbacks.containsKey(dispatchToken)) { "$dispatchToken does not map to a registered callback." }
        callbacks.remove(dispatchToken)
    }


    /**
     * Waits for the callbacks specified to be invoked before continuing execution
     * of the current callback. This method should only be used by a callback in
     * response to a dispatched payload.
     */
    fun waitFor(vararg ids: DispatchToken) {
        check(isDispatching()) {
            "Dispatcher.waitFor(...): Must be invoked while dispatching"
        }
        ids.forEach { id ->
            if (isPending[id] == true) {
                check(this.isHandled[id]!!) {
                    "Dispatcher.waitFor(...): Circular dependency detected while waiting for `$id`"
                }
                return@forEach
            }
            check(callbacks.containsKey(id)) {
                "Dispatcher.waitFor(...): `%s` does not map to a registered callback."
            }
            invokeCallback(id)
        }
    }

    /**
     * Dispatches a payload to all registered [PayloadCallback].
     */
    fun dispatch(payload: Payload) {
        check(!isDispatching.get()) {
            "Cannot dispatch in the middle of a dispatch."
        }
        startDispatching(payload)
        try {
            callbacks.forEach {
                if (!isPending[it.key]!!) {
                    this.invokeCallback(it.key)
                }
            }
        } finally {
            stopDispatching()
        }

        // let the middleware run
        middleware.forEach { it(payload) { dispatch(it) } }
    }

    /**
     * Set up bookkeeping needed when dispatching.
     */
    private fun startDispatching(payload: Payload) {
        callbacks.forEach {
            isPending[it.key] = false
            isHandled[it.key] = false
        }
        pendingPayload = payload
        isDispatching.set(true)
    }

    /**
     * Clear bookkeeping used for dispatching.
     */
    private fun stopDispatching() {
        pendingPayload = null
        isDispatching.set(false)
    }

    /**
     * Call the callback stored with the given id. Also do some internal
     * bookkeeping.
     */
    private fun invokeCallback(id: DispatchToken)  {
        isPending[id] = true
        (callbacks[id]!!)(pendingPayload!!)
        isHandled[id] = true
    }

    /**
     * Is this Dispatcher currently dispatching.
     */
    fun isDispatching(): Boolean = isDispatching.get()

}
