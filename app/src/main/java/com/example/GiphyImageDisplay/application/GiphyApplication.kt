package com.example.GiphyImageDisplay.application

import android.app.Application
import com.example.GiphyImageDisplay.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class GiphyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            // declare used Android context
            androidContext(this@GiphyApplication)
            // declare modules
            modules(provideComponent())
        }

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}