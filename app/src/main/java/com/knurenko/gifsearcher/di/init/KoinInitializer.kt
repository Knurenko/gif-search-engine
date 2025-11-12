package com.knurenko.gifsearcher.di.init

import android.content.Context
import com.knurenko.gifsearcher.di.modules.networkModule
import com.knurenko.gifsearcher.di.modules.repoModule
import com.knurenko.gifsearcher.di.modules.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

/**
 * @author Knurenko Bogdan 12.11.2025
 */
private val activeModules: List<Module> = listOf(
    networkModule,
    repoModule,
    viewModelsModule
)

fun initKoin(ctx: Context) {
    startKoin {
        androidContext(ctx.applicationContext)
        modules(activeModules)
    }
}