package com.puddlealley.splash

interface State
/**
 * This is the basic building block of a Flux application. All of your stores
 * should extend this class.
 *
 *   class CounterStore : FluxReduceStore<number> {
 *     getInitialState(): number {
 *       return 1;
 *     }
 *
 *     reduce(state: number, action: Object): number {
 *       switch(action.type) {
 *         case: 'add':
 *           return state + action.value;
 *         case: 'double':
 *           return state * 2;
 *         default:
 *           return state;
 *       }
 *     }
 *   }
 */
abstract class FluxReduceStore<T: State>(initialState: T, dispatcher: Dispatcher): FluxStore(dispatcher) {

    /**
     * Getter that exposes the entire state of this store. If your state is not
     * immutable you should override this and not expose _state directly.
     */
     protected var innerState: T = initialState
        private set

     open val state get() = innerState

    /**
     * Used to reduce a stream of actions coming from the dispatcher into a
     * single state object.
     */
    protected abstract fun reduce(state: T, action: Payload): T

    /**
     * Checks if two versions of state are the same. You do not need to override
     * this if your state is immutable.
     */
    open fun areEqual(one: T, two: T): Boolean {
        return one == two
    }

    override fun invokeOnDispatch(payload: Payload) {
        changed = false

        // Reduce the stream of incoming actions to state, update when necessary.
        val startingState = this.innerState
        val endingState = this.reduce(startingState, payload)

        if (!this.areEqual(startingState, endingState)) {
            this.innerState = endingState

            // `__emitChange()` sets `this.__changed` to true and then the actual
            // change will be fired from the emitter at the end of the dispatch, this
            // is required in order to support methods like `hasChanged()`
            this.emitChange()
        }

        if (this.changed) {
            this.callbacks.forEach { it() }
        }
    }

}