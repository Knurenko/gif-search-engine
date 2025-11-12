package com.knurenko.gifsearcher.di.init

import android.app.Application

/**
 * @author Knurenko Bogdan 12.11.2025
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}