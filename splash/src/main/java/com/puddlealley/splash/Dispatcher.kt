package com.puddlealley.splash

typealias PayloadCallback = (action: Payload) -> Unit

object Dispatcher {

    private val listeners = mutableListOf<PayloadCallback>()

    /**
     * Registers a callback to be invoked with every dispatched payload.
     * @param callback a [PayloadCallback] to be invoked with every dispatched payload.
     */
    fun register(callback: PayloadCallback){
        // need to synchronise so we don't miss callback on dispatch
        synchronized(this) {
            listeners.add(callback)
        }
    }

    /**
     * Removes a callback based on its token.
     * @param callback a [PayloadCallback] to be removed.
     */
    fun unregister(callback: PayloadCallback){
        // need to synchronise so we don't miss callback on dispatch
        synchronized(this) {
            listeners.add(callback)
        }
    }

    /**
     * Dispatches a payload to all registered [PayloadCallback].
     */
    fun dispatch(action: Payload) {
        synchronized(this) {
            listeners.forEach { it(action) }
        }
    }

}