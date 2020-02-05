package com.puddlealley.splash


typealias Reducer<S> = (oldState: S, action: Payload) -> S

/**
 * Creates a Redux store that holds the state tree.
 * The only way to change the data in the store is to call `dispatch()` on it.
 *
 * There should only be a single store in your app. To specify how different
 * parts of the state tree respond to actions, you may combine several reducers
 * into a single reducer fun by using `combineReducers`.
 *
 * @param reducer A fun that returns the next state tree, given
 * the current state tree and the action to handle.
 *
 * @param preloadedState The initial state. You may optionally specify it
 * to hydrate the state from the server in universal apps, or to restore a
 * previously serialized user session.
 * If you use `combineReducers` to produce the root reducer fun, this must be
 * an object with the same shape as `combineReducers` keys.
 *
 * @param enhancer The store enhancer. You may optionally specify it
 * to enhance the store with third-party capabilities such as middleware,
 * time travel, persistence, etc. The only store enhancer that ships with Redux
 * is `applyMiddleware()`.
 *
 * @returns A Redux store that lets you read the state, dispatch actions
 * and subscribe to changes.
 */
class ReduxStore<T : State>(
    val reducer: Reducer<T>,
    initialState: T,
    middleware: Middleware = { {} }
    //enhancer?: StoreEnhancer<Ext, StateExt>
) : FluxReduceStore<T>(initialState, Dispatcher(middleware)) {

    private var isReducing = false

    override val state: T
        get() {
            if (isReducing) {
                throw error(
                    "You may not call store.getState() while the reducer is executing. " +
                            "The reducer has already received the state as an argument. " +
                            "Pass it down from the top reducer instead of reading it from the store."
                )
            }
            return super.state
        }

    /**
     * Dispatches an action. It is the only way to trigger a state change.
     *
     * The `reducer` fun, used to create the store, will be called with the
     * current state tree and the given `action`. Its return value will
     * be considered the **next** state of the tree, and the change listeners
     * will be notified.
     *
     * The base implementation only supports plain object actions. If you want to
     * dispatch a Promise, an Observable, a thunk, or something else, you need to
     * wrap your store creating fun into the corresponding middleware. For
     * example, see the documentation for the `redux-thunk` package. Even the
     * middleware will eventually dispatch plain object actions using this method.
     *
     * @param action A plain object representing “what changed”. It is
     * a good idea to keep actions serializable so you can record and replay user
     * sessions, or use the time travelling `redux-devtools`. An action must have
     * a `type` property which may not be `undefined`. It is a good idea to use
     * string valants for action types.
     *
     * @returns For convenience, the same action object you dispatched.
     *
     * Note that, if you use a custom middleware, it may wrap `dispatch()` to
     * return something else (for example, a Promise you can await).
     */
    fun dispatch(action: Payload) {
        getDispatcher().dispatch(action)
    }

    override fun reduce(state: T, action: Payload): T {
        isReducing = true
        val state = reducer(state, action)
        isReducing = false
        return state
    }
}