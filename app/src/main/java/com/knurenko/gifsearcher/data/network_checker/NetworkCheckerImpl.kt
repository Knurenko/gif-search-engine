package com.knurenko.gifsearcher.data.network_checker

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * @author Knurenko Bogdan 11.11.2025
 */
class NetworkCheckerImpl(
    private val context: Context
) : NetworkChecker {
    override suspend fun isNetworkAvailable(): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connMgr.activeNetwork
        val capabilities = connMgr.getNetworkCapabilities(network)
        return capabilities?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ) ?: false
    }
}