package com.puddlealley.splash

interface State

typealias StoreCallback<T> = (T) -> Unit

abstract class Store<T: State>(initialState: T) {

    private var innerState : T = initialState
    private val listeners = mutableListOf<StoreCallback<T>>()

    init {
        Dispatcher.register {
            val newState = reduce(innerState, it)
            if(!areEqual(innerState, newState)) {
                innerState = newState
                listeners.forEach { callback -> callback(innerState) }
            }
        }
    }

    /**
     * Getter that exposes the entire state of this store.
     */
    val state : T = innerState

    /**
     * Reduces the current state, and an action to the new state of this store. All subclasses must
     * implement this method. This method should be pure and have no side-effects.
     */
    protected abstract fun reduce(state: T, payload: Payload): T

    /**
     * Checks if two versions of state are the same.
     * You do not need to override this if your state implement hashcode and equals appropriately.
     */
    protected open fun areEqual(old: T, new: T): Boolean = old == new

    /**
     * Adds a listener to the store, when the store changes the given callback will be called.
     */
    fun addListener(callback: StoreCallback<T>) {
        listeners.add(callback)
    }
    /**
     * Removes a listener to the store
     */
    fun removeListener(callback: StoreCallback<T>) {
        listeners.add(callback)
    }
}