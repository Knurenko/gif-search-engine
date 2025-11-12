package com.knurenko.gifsearcher.di.modules

import com.knurenko.gifsearcher.BuildConfig
import com.knurenko.gifsearcher.data.network_checker.NetworkChecker
import com.knurenko.gifsearcher.data.network_checker.NetworkCheckerImpl
import com.knurenko.gifsearcher.data.service.GifApiService
import com.knurenko.gifsearcher.data.service.KtorGifApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * @author Knurenko Bogdan 12.11.2025
 */
val networkModule = module {
    single<HttpClient> {
        HttpClient(engineFactory = OkHttp, block = {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
            install(Logging) {
                LogLevel.BODY
            }
        })
    }

    single<GifApiService> {
        val baseUrl = "https://api.giphy.com/v1/gifs"

        val apiKey = BuildConfig.GIPHY_API_KEY

        KtorGifApiService(client = get(), baseUrl = baseUrl, apiKey = apiKey)
    }

    single<NetworkChecker> { NetworkCheckerImpl(context = get()) }
}