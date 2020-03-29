package com.puddlealley.flux

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createKoin(this)
        Timber.plant(Timber.DebugTree())
    }
}