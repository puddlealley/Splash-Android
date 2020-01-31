package com.puddlealley.splash

import android.util.Log
import org.junit.Test

class DispatcherTest{

    @Test
    fun `callback that dispatches`() {
        class First : Payload {}
        class Second : Payload {}
        Dispatcher.register { Second ->
            Dispatcher.dispatch(First())
        }
        Dispatcher.dispatch(First())
    }

    @Test
    fun `simple middleware`() {
        class First : Payload {}
        class Second : Payload {}
        class Third : Payload {}

        Dispatcher.register { action ->
            print("$action\n")
        }
        Dispatcher.middleware.add { _, dispatch ->
            dispatch(Second())
            dispatch(Third())
        }
        Dispatcher.dispatch(First())
    }
}