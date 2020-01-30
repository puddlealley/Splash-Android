package com.puddlealley.splash

import org.junit.Assert.*
import org.junit.Test

class DispatcherTest{

    @Test
    fun `callback that dispatches`() {

        object TestState : State
        val store = Store<TestState>(TestState)

    }
}