package com.puddlealley.splash


interface Payload {
    fun log() { }
}

interface Event : Payload

interface Result : Payload


