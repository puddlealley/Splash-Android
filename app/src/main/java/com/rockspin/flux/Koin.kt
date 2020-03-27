package com.rockspin.flux

import android.content.Context
import com.rockspin.flux.service.Server
import com.rockspin.flux.service.UserDataFetcher
import com.rockspin.flux.store.AppStore
import com.rockspin.flux.ui.success.fragment1.Fragment1ViewModel
import com.rockspin.flux.ui.success.fragment2.Fragment2ViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Create the di dependency tree for this app using koin
 */
fun createKoin(context: Context) = startKoin {
    modules(
        module {
            androidContext(context)
            single { AppStore(get()) }
            single { Server() }
            factory { UserDataFetcher(get()) }
            // select sensor
            viewModel { Fragment1ViewModel() }
            // setup sensor
            viewModel { Fragment2ViewModel() }
        }
    )
}