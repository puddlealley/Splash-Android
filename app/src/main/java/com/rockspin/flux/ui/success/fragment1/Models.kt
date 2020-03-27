package com.rockspin.flux.ui.success.fragment1

import com.puddlealley.splash.core.Event
import com.puddlealley.splash.core.State

sealed class Fragment1Events : Event {
    object ClickedOpen : Fragment1Events()
}

data class Fragment1State(val loading: Boolean = false) : State



