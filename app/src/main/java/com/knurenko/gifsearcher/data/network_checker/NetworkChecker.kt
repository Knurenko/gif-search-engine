package com.knurenko.gifsearcher.data.network_checker

/**
 * @author Knurenko Bogdan 06.11.2025
 */
interface NetworkChecker {
    suspend fun isNetworkAvailable(): Boolean
}