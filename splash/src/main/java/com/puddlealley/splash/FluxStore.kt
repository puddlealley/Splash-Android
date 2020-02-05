package com.puddlealley.splash



typealias StoreCallback = () -> Unit

/**
 * This class represents the most basic functionality for a FluxStore. Do not
 * extend this store directly; instead extend FluxReduceStore when creating a
 * new store.
 */
    abstract class FluxStore(
    private val dispatcher: Dispatcher
) {

    private val dispatchToken: DispatchToken
    protected val callbacks = mutableListOf<StoreCallback>()
    protected var changed = false

    init {
        dispatchToken = dispatcher.register {
            invokeOnDispatch(it)
        }
    }
    fun addListener(callback: ()-> Unit): () -> Unit {
        callbacks.add(callback)
        return { callbacks.remove(callback) }
    }

    fun getDispatcher(): Dispatcher = dispatcher

    /**
     * Returns whether the store has changed during the most recent dispatch.
     */
    fun hasChanged(): Boolean {
        check(dispatcher.isDispatching()) { "${javaClass.name}.hasChanged(): Must be invoked while dispatching." }
        return changed;
    }

    /**
     * This exposes a unique [UUid] to identify each store's registered callback.
     * This is used with the dispatcher's waitFor method to declaratively depend
     * on other stores updating themselves first.
     */
    fun getDispatchToken(): DispatchToken = dispatchToken

    protected fun emitChange() {
        check(this.dispatcher.isDispatching()) { "${javaClass.name}.__emitChange(): Must be invoked while dispatching" }
        changed = true
    }

    /**
     * This method encapsulates all logic for invoking __onDispatch. It should
     * be used for things like catching changes and emitting them after the
     * subclass has handled a payload.
     */
    protected open fun invokeOnDispatch(payload: Payload) {
        this.changed = false
        this.onDispatch(payload)
        if (this.changed) {
            this.callbacks.forEach { it() }
        }
    }

    /**
     * The callback that will be registered with the dispatcher during
     * instantiation. Subclasses must override this method. This callback is the
     * only way the store receives new data.
     */
    protected open fun onDispatch(payload: Payload) {
        check(false) { "${javaClass.name} has not overridden FluxStore.__onDispatch(), which is required" }
    }

}

